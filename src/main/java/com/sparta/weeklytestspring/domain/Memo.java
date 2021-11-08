package com.sparta.weeklytestspring.domain;

import com.sparta.weeklytestspring.dto.MemoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Memo extends Timestamp {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    private Long clickCount;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Memo(String title, String contents, Long clickCount, User user) {
        this.title = title;
        this.contents = contents;
        this.clickCount = clickCount;
        this.user = user;
    }

    public void updateMemo(MemoDto memoDto) {
        this.title = memoDto.getTitle();
        this.contents = memoDto.getContents();
    }

    public void increaseClickCount() {
        this.clickCount++;
    }
}
