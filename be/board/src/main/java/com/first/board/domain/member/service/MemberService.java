package com.first.board.domain.member.service;

import com.first.board.domain.member.adaptor.MemberAdaptor;
import com.first.board.domain.member.dto.request.MemberModifyRequest;
import com.first.board.domain.member.dto.request.RegisterRequest;
import com.first.board.domain.member.dto.response.MemberInfoResponse;
import com.first.board.domain.member.entity.Member;
import com.first.board.domain.member.repository.MemberRepository;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.AuthenticationException;
import com.first.board.global.secuirty.encryption.Encryption;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberAdaptor memberAdaptor;
    private final MemberRepository memberRepository;
    private final Encryption encryption;

    @Transactional
    public void register(RegisterRequest registerRequest) {

        if (memberRepository.existsByMemberId(registerRequest.getMemberId())) {
            throw new AuthenticationException(ErrorCode.ALREADY_REGISTERED_MEMBER);
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
    }

    public MemberInfoResponse info(String memberId) {
        Member member = memberAdaptor.findByMemberId(memberId);

        String phoneNumber = encryption.decrypt(member.getEncryptPhoneNumber(), member.getSalt());

        return MemberInfoResponse.from(member, phoneNumber);
    }

    @Transactional
    public void modify(String memberId, MemberModifyRequest memberModifyRequest) {
        Member member = memberAdaptor.findByMemberId(memberId);

        String encryptPassword = encryption.encryptPassword(memberModifyRequest.getPassword(), member.getSalt());
        String encryptPhoneNumber = encryption.encrypt(memberModifyRequest.getPhoneNumber(), member.getSalt());

        memberModifyRequest.setPassword(encryptPassword);
        memberModifyRequest.setPhoneNumber(encryptPhoneNumber);

        member.modify(memberModifyRequest);
    }
}
