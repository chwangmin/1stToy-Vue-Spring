package com.first.board.domain.comment.repository;

import com.first.board.domain.comment.entity.Comment;
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
public class CommentRepository {
    private final MongoDatabase mongoDB;
    private final MongoUtil mongoUtil;
    private static final String COLLECTION_COMMENT = "comment";

    private MongoCollection<Comment> getCollection() {
        return mongoDB.getCollection(COLLECTION_COMMENT, Comment.class);
    }

    public void save(Comment comment) {
        getCollection().insertOne(comment);
    }

    public List<Comment> findByBoardId(String boardId) {
        return getCollection()
                .find(Filters.eq("boardId", boardId))
                .sort(Sorts.ascending("createDate"))
                .into(new ArrayList<>());
    }

    public Optional<Comment> findById(String commentId) {
        ObjectId objectId = mongoUtil.ConvertStringToObjectId(commentId);
        return Optional.ofNullable(getCollection().find(Filters.eq("_id", objectId)).first());
    }

    public void modify(Comment comment) {
        Bson filter = Filters.eq("_id", comment.getId());
        Bson updates = Updates.combine(
                Updates.set("content", comment.getContent()),
                Updates.set("modifyDate", comment.getModifyDate())
        );
        getCollection().updateOne(filter, updates);
    }

    public void delete(Comment comment) {
        getCollection().deleteOne(Filters.eq("_id", comment.getId()));
    }

    public List<Comment> findByBoardIdWithPagination(String boardId, int page, int size) {
        return getCollection()
                .find(Filters.eq("boardId", boardId))
                .sort(Sorts.ascending("createDate"))
                .skip(page * size)
                .limit(size)
                .into(new ArrayList<>());
    }

    public long countByBoardId(String boardId) {
        ObjectId objectId = mongoUtil.ConvertStringToObjectId(boardId);
        return getCollection().countDocuments(Filters.eq("boardId", objectId));
    }

    public void updateReplyNumDown(String commentId) {
        ObjectId objectId = mongoUtil.ConvertStringToObjectId(commentId);
        Bson filter = Filters.eq("_id", objectId);
        Bson updates = Updates.combine(
                Updates.inc("replyNum", -1)
        );
        getCollection().updateOne(filter, updates);
    }

    public void updateReplyNumUp(String commentId) {
        ObjectId objectId = mongoUtil.ConvertStringToObjectId(commentId);
        Bson filter = Filters.eq("_id", objectId);
        Bson updates = Updates.combine(
                Updates.inc("replyNum", +1)
        );
        getCollection().updateOne(filter, updates);
    }

    public List<Comment> findByParentId(String parentId) {
        return getCollection()
                .find(Filters.eq("parentId", parentId))
                .sort(Sorts.ascending("createDate"))
                .into(new ArrayList<>());
    }
}