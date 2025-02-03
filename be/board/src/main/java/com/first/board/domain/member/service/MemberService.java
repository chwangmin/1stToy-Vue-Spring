package com.first.board.domain.member.service;

import com.first.board.domain.member.adaptor.MemberAdaptor;
import com.first.board.domain.member.dto.request.ModifyMemberRequest;
import com.first.board.domain.member.dto.request.RegisterRequest;
import com.first.board.domain.member.dto.response.MemberInfoResponse;
import com.first.board.domain.member.entity.Member;
import com.first.board.domain.member.repository.MemberRepository;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.AuthenticationException;
import com.first.board.global.error.exception.BusinessException;
import com.first.board.global.mail.EmailConstraints;
import com.first.board.global.mail.SendEmailLogic;
import com.first.board.global.secuirty.encryption.Encryption;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberAdaptor memberAdaptor;
    private final MemberRepository memberRepository;
    private final Encryption encryption;
    private final SendEmailLogic sendEmailLogic;
    private final JavaMailSender javaMailSender;

    @Transactional
    public void register(RegisterRequest registerRequest) {

        if (memberRepository.existsByMemberId(registerRequest.getMemberId())) {
            throw new AuthenticationException(ErrorCode.ALREADY_REGISTERED_MEMBER);
        }

        if (memberRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AuthenticationException(ErrorCode.ALREADY_REGISTERED_EMAIL);
        }

        String salt = encryption.getSalt();

        String encryptPassword = encryption.encryptPassword(registerRequest.getPassword(), salt);
        String encryptBirthDate = encryption.encrypt(String.valueOf(registerRequest.getBirthdate()), salt);
        String encryptPhoneNumber = encryption.encrypt(registerRequest.getPhoneNumber(), salt);

        Member member = registerRequest.toEntity(encryptPassword, encryptBirthDate, encryptPhoneNumber, salt);

        memberRepository.save(member);
    }

    public void leave(String memberId) {
        Member member = memberAdaptor.findByMemberId(memberId);
        member.leave();
        memberRepository.leave(member);
    }

    public MemberInfoResponse info(String memberId) {
        Member member = memberAdaptor.findByMemberId(memberId);

        String phoneNumber = encryption.decrypt(member.getEncryptPhoneNumber(), member.getSalt());

        return MemberInfoResponse.from(member, phoneNumber);
    }

    @Transactional
    public void modify(String memberId, ModifyMemberRequest modifyMemberRequest) {
        Member member = memberAdaptor.findByMemberId(memberId);

        String encryptPassword = encryption.encryptPassword(modifyMemberRequest.getPassword(), member.getSalt());
        String encryptPhoneNumber = encryption.encrypt(modifyMemberRequest.getPhoneNumber(), member.getSalt());

        modifyMemberRequest.setPassword(encryptPassword);
        modifyMemberRequest.setPhoneNumber(encryptPhoneNumber);

        member.modify(modifyMemberRequest);
        memberRepository.modify(member);
    }

    @Transactional
    @Async("mailExecutor")
    public void sendPassword(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_MATCH));

        String newPassword = sendEmailLogic.makeRandomPassword();

        try {
            String encPassword = encryption.hashing(newPassword.getBytes(),member.getSalt());
            member.modifyPassword(encPassword);
            memberRepository.modify(member);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_AES_KEY);
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject(EmailConstraints.MAIL_TITLE); // 메일 제목
            mimeMessageHelper.setText(sendEmailLogic.setContext(newPassword, "password"), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new BusinessException(ErrorCode.EMAIL_FAIL);
        }
    }
}
