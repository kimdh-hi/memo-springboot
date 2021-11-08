package com.sparta.weeklytestspring.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Comment extends Timestamp {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String contents;

    @JoinColumn(name = "memo_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Memo memo;
}
