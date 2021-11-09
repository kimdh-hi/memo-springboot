package com.sparta.weeklytestspring.repository;

import com.sparta.weeklytestspring.domain.Memo;
import com.sparta.weeklytestspring.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    Page<Memo> findAll(Pageable pageable);

    @Query("select m from Memo m where m.user.id = :id")
    Page<Memo> findAllByUser(Long id, Pageable pageable);
}
