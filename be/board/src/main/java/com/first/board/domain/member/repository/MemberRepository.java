package com.first.board.domain.member.repository;

import com.first.board.domain.member.entity.Member;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.RequiredArgsConstructor;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final MongoDatabase mongoDB;
    private static final String COLLECTION_MEMBER = "member";

    private MongoCollection<Member> getCollection() {
        return mongoDB.getCollection(COLLECTION_MEMBER, Member.class);
    }

    public Optional<Member> findByMemberId(String memberId){
        return Optional.ofNullable(
                getCollection()
                        .find(Filters.eq("memberId", memberId))
                        .map(document -> document)
                        .first()
        );
    }

    public boolean existsByMemberId(String memberId){
        return getCollection().countDocuments(Filters.eq("memberId", memberId)) > 0;
    }

    public void deleteByIsActiveFalse() {
        getCollection()
                .deleteMany(Filters.eq("isActive", false));
    }

    public void save(Member member){
        getCollection().insertOne(member);
    }

    public void leave(Member member){
        Bson filter = Filters.eq("memberId", member.getMemberId());

        Bson updates = Updates.combine(
                Updates.set("isActive", member.getIsActive())
        );

        getCollection().updateOne(filter, updates);
    }

    public void modify(Member member) {
        Bson filter = Filters.eq("memberId", member.getMemberId());

        Bson updates = Updates.combine(
                Updates.set("encryptPassword", member.getEncryptPassword()),
                Updates.set("koName", member.getKoName()),
                Updates.set("enName", member.getEnName()),
                Updates.set("email", member.getEmail()),
                Updates.set("encryptPhoneNumber", member.getEncryptPhoneNumber())
        );

        getCollection().updateOne(filter, updates);
    }

}
