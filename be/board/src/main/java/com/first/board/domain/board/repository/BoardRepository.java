package com.first.board.domain.board.repository;

import com.first.board.domain.board.constant.BoardConstant;
import com.first.board.domain.board.entity.Board;
import com.first.board.domain.board.type.BoardType;
import com.first.board.domain.board.type.SortType;
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
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class BoardRepository {
    private final MongoDatabase mongoDB;
    private final MongoUtil mongoUtil;
    private static final String COLLECTION_BOARD = "board";

    private MongoCollection<Board> getCollection() {
        return mongoDB.getCollection(COLLECTION_BOARD, Board.class);
    }

    public void save(Board board) {
        getCollection().insertOne(board);
    }

    public List<Board> searchBoards(String searchText, int page, SortType sortType, BoardType boardType) {
        try {
            return executeSearch(searchText, page, sortType, boardType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void modifyBoard(Board board) {
        Bson filter = Filters.eq("_id", board.getId());

        Bson updates = Updates.combine(
                Updates.set("title", board.getTitle()),
                Updates.set("content", board.getContent()),
                Updates.set("fileName", board.getFileName()),
                Updates.set("filePath", board.getFilePath())
        );

        getCollection().updateOne(filter, updates);
    }

    public void delete(Board board) {
        getCollection().deleteOne(Filters.eq("_id", board.getId()));
    }

    public Optional<Board> findById(String boardId) {
        ObjectId objectId = mongoUtil.ConvertStringToObjectId(boardId);
        return Optional.ofNullable(getCollection().find(Filters.eq("_id", objectId)).first());
    }

    public void view(Board board) {
        Bson filter = Filters.eq("_id", board.getId());
        Bson updates = Updates.inc("views", 1);
        getCollection().updateOne(filter, updates);
    }

    private List<Board> executeSearch(String searchText, int page, SortType sortType, BoardType boardType) {
        MongoCollection<Board> collection = getCollection();
        Bson sort = createSort(sortType);
        int skip = calculateSkip(page);

        Bson filter = createFilter(searchText, boardType);

        return collection.find(filter)
                .sort(sort)
                .skip(skip)
                .limit(BoardConstant.Page.DEFAULT_SIZE)
                .into(new ArrayList<>());
    }

    private Bson createFilter(String searchText, BoardType boardType) {
        List<Bson> conditions = new ArrayList<>();

        // BoardType 필터 추가
        if (boardType != null) {
            conditions.add(Filters.eq("boardType", boardType));
        }

        // 검색어 필터 추가
        if (searchText != null && !searchText.trim().isEmpty()) {
            String sanitizedText = Pattern.quote(searchText.trim());
            conditions.add(Filters.or(
                    Filters.regex("title", sanitizedText, "i"),
                    Filters.regex("content", sanitizedText, "i")
            ));
        }

        // 조건이 없으면 전체 검색
        if (conditions.isEmpty()) {
            return Filters.empty();
        }

        // 여러 조건을 AND로 결합
        return conditions.size() == 1 ? conditions.get(0) : Filters.and(conditions);
    }

    private List<Board> findAllWithPagination(MongoCollection<Board> collection, Bson sort, int skip) {
        return collection.find()
                .sort(sort)
                .skip(skip)
                .limit(BoardConstant.Page.DEFAULT_SIZE)
                .into(new ArrayList<>());
    }

    private List<Board> findWithSearchCriteria(MongoCollection<Board> collection, String searchText, Bson sort, int skip) {
        String sanitizedText = Pattern.quote(searchText.trim());
        Bson searchQuery = Filters.or(
                Filters.regex("title", sanitizedText, "i"),
                Filters.regex("content", sanitizedText, "i")
        );

        return collection.find(searchQuery)
                .sort(sort)
                .skip(skip)
                .limit(BoardConstant.Page.DEFAULT_SIZE)
                .into(new ArrayList<>());
    }

    private int calculateSkip(int page) {
        return Math.max(0, page) * BoardConstant.Page.DEFAULT_SIZE;
    }

    private Bson createSort(SortType sortType) {
        switch (sortType) {
            case CREATED_DESC:
                return Sorts.descending("createDate");
            case CREATED_ASC:
                return Sorts.ascending("createDate");
            case VIEWS_DESC:
                return Sorts.descending("views");
            case VIEWS_ASC:
                return Sorts.ascending("views");
            default:
                return Sorts.descending("createDate"); // 기본 정렬
        }
    }

    public long findMaxNum(String keyword, BoardType boardType) {
        List<Bson> conditions = new ArrayList<>();

        if (boardType != null) {
            conditions.add(Filters.eq("boardType", boardType));
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            String sanitizedText = Pattern.quote(keyword.trim());
            conditions.add(Filters.or(
                    Filters.regex("title", sanitizedText, "i"),
                    Filters.regex("content", sanitizedText, "i")
            ));
        }

        Bson filter = conditions.isEmpty() ?
                Filters.empty() :
                conditions.size() == 1 ? conditions.get(0) : Filters.and(conditions);

        return getCollection().countDocuments(filter);
    }

}
