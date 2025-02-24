package com.first.board.domain.rocketchat.repository;

import com.first.board.domain.rocketchat.entity.RocketChat;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
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

    private MongoCollection<RocketChat> getCollection() { return mongoDB.getCollection(COLLECTION_NAME, RocketChat.class); }

    public List<RocketChat> findByUserId(int userId) {
        Bson filter = Filters.eq("userId", userId);

        return getCollection().find(filter).into(new ArrayList<RocketChat>());
    }

    public void save(RocketChat rocketChat) {
        getCollection().insertOne(rocketChat);
    }

    public Optional<RocketChat> findById(ObjectId id) {
        return Optional.ofNullable(getCollection().find(Filters.eq("_id", id)).first());
    }

    public void update(RocketChat rocketChat) {
        Bson filter = Filters.eq("_id", rocketChat.getId());

        Bson updates = Updates.combine(
                Updates.set("pk", rocketChat.getPk()),
                Updates.set("memberId", rocketChat.getMemberId()),
                Updates.set("week", rocketChat.getWeek()),
                Updates.set("loop", rocketChat.getLoop()),
                Updates.set("time", rocketChat.getTime()),
                Updates.set("icon", rocketChat.getIcon()),
                Updates.set("message", rocketChat.getMessage()),
                Updates.set("isGpt", rocketChat.getIsGpt()),
                Updates.set("XAuthToken", rocketChat.getXAuthToken()),
                Updates.set("XUserId", rocketChat.getXUserId()),
                Updates.set("isConnectable", rocketChat.getIsConnectable())
        );

        getCollection().updateOne(filter, updates);
    }


    public void deleteById(ObjectId rocketChatId) {
        getCollection().deleteOne(Filters.eq("_id", rocketChatId));
    }
}
