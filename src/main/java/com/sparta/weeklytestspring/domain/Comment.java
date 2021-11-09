package com.sparta.weeklytestspring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
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

    @ManyToOne
    @JoinColumn(name = "memo_id")
    private Memo memo;

    public void setMemo(Memo memo) {
        this.memo = memo;
    }

    public Comment(String contents) {
        this.contents = contents;
    }


}
