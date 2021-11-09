package com.sparta.weeklytestspring.repository;

import com.sparta.weeklytestspring.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.memo.id = :memoId")
    List<Comment> findByMemoId(Long memoId);
}
