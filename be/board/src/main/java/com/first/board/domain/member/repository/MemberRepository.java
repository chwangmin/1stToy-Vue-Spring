package com.first.board.domain.member.repository;

import com.first.board.domain.member.entity.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {

    Optional<Member> findByMemberId(String memberId);

    boolean existsByMemberId(String email);

    void deleteByIsActiveFalse();
}
