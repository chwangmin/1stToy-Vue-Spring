package com.first.board.domain.rocketchat.repository;

import com.first.board.domain.rocketchat.dto.request.ModifyRocketChatRequest;
import com.first.board.domain.rocketchat.entity.RocketChat;
import com.first.board.domain.rocketchat.entity.ScheduledMessageStatus;
import com.first.board.global.mongodb.MongoUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import lombok.RequiredArgsConstructor;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RocketChatRepository {
    private final MongoDatabase mongoDB;
    private static final String COLLECTION_NAME = "rocketchat";
    private final MongoUtil mongoUtil;

    private MongoCollection<RocketChat> getCollection() { return mongoDB.getCollection(COLLECTION_NAME, RocketChat.class); }

    public List<RocketChat> findByUserId(int userId) {
        Bson filter = Filters.eq("userId", userId);

        return getCollection().find(filter).into(new ArrayList<RocketChat>());
    }

    public List<RocketChat> findByXUserId(String XUserId) {
        Bson filter = Filters.eq("xUserId", XUserId);

        Bson sort = Sorts.orderBy(
                Sorts.descending("status", "PENDING"),
                Sorts.ascending("status")
        );

        return getCollection().find(filter).sort(sort).into(new ArrayList<RocketChat>());
    }

    public RocketChat save(RocketChat rocketChat) {
        getCollection().insertOne(rocketChat);

        return rocketChat;
    }

    public Optional<RocketChat> findById(String id) {
        ObjectId objectId = mongoUtil.ConvertStringToObjectId(id);
        return Optional.ofNullable(getCollection().find(Filters.eq("_id", objectId)).first());
    }

    public RocketChat update(ModifyRocketChatRequest modifyRocketChatRequest) {
        ObjectId objectId = mongoUtil.ConvertStringToObjectId(modifyRocketChatRequest.getId());
        Bson filter = Filters.eq("_id", objectId);

        Bson updates = Updates.combine(
                Updates.set("memberId", modifyRocketChatRequest.getMemberId()),
                Updates.set("week", modifyRocketChatRequest.getWeek()),
                Updates.set("date", modifyRocketChatRequest.getDate()),
                Updates.set("time", modifyRocketChatRequest.getTime()),
                Updates.set("icon", modifyRocketChatRequest.getIcon()),
                Updates.set("message", modifyRocketChatRequest.getMessage()),
                Updates.set("isGpt", modifyRocketChatRequest.getIsGpt()),
                Updates.set("XAuthToken", modifyRocketChatRequest.getXAuthToken()),
                Updates.set("XUserId", modifyRocketChatRequest.getXUserId()),
                Updates.set("status", ScheduledMessageStatus.PENDING)
        );

        getCollection().updateOne(filter, updates);

        return getCollection().find(filter).first();
    }


    public void deleteById(String id) {
        ObjectId objectId = mongoUtil.ConvertStringToObjectId(id);
        getCollection().deleteOne(Filters.eq("_id", objectId));
    }

    public List<RocketChat> findAllByStatus(ScheduledMessageStatus status) {
        Bson filter = Filters.eq("status", status);

        return getCollection().find(filter).into(new ArrayList<RocketChat>());
    }

    public void updateStatus(RocketChat rocketChat) {
        Bson filter = Filters.eq("_id", rocketChat.getId());

        Bson updates = Updates.combine(
                Updates.set("status", rocketChat.getStatus())
        );

        getCollection().updateOne(filter, updates);
    }

    public long findCountByXUserId(String XUserId) {
        Bson filter = Filters.eq("xUserId", XUserId);

        return getCollection().countDocuments(filter);
    }
}
