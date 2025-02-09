package com.first.board.domain.member.repository;

import com.first.board.domain.member.entity.Member;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import lombok.RequiredArgsConstructor;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.Objects;
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

    public Member save(Member member){
        try {
            InsertOneResult result = getCollection().insertOne(member);
            member.setId(Objects.requireNonNull(result.getInsertedId()).asObjectId().getValue());
            return member;
        } catch (MongoException me) {
            throw new RuntimeException("Failed to save member", me);
        }
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

    public boolean existsByEmail(String email) {
        return getCollection().countDocuments(Filters.eq("email", email)) > 0;
    }

    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(
                getCollection()
                        .find(Filters.eq("email", email))
                        .map(document -> document)
                        .first()
        );
    }


    public void updateFailCnt(Member member) {
        Bson filter = Filters.eq("memberId", member.getMemberId());

        Bson updates = Updates.combine(
                Updates.inc("failCnt", 1)
        );

        getCollection().updateOne(filter, updates);
    }

    public void initFailCnt(Member member) {
        Bson filter = Filters.eq("memberId", member.getMemberId());

        Bson updates = Updates.combine(
                Updates.set("failCnt", 0)
        );

        getCollection().updateOne(filter, updates);
    }

    public void deleteAll() {
        try {
            getCollection().deleteMany(Filters.empty());
        } catch (MongoException me) {
            throw new RuntimeException("Failed to delete all members", me);
        }
    }
}
