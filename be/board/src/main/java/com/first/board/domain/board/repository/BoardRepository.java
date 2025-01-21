package com.first.board.domain.board.repository;

import com.first.board.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends MongoRepository<Board, String> {
    // 검색어가 제목이나 내용에 포함된 게시글 찾기
    Page<Board> findByTitleContainingOrContentContaining(
            String title,
            String content,
            Pageable pageable
    );
}
