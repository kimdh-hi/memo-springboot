package com.sparta.weeklytestspring.domain;

import com.sparta.weeklytestspring.dto.MemoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    private Boolean isAnonymous;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "memo", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Memo(String title, String contents, Long clickCount, User user, Boolean isAnonymous) {
        this.title = title;
        this.contents = contents;
        this.clickCount = clickCount;
        this.user = user;
        this.isAnonymous = isAnonymous;
    }

    public void updateMemo(MemoDto memoDto) {
        this.title = memoDto.getTitle();
        this.contents = memoDto.getContents();
    }

    public void increaseClickCount() {
        this.clickCount++;
    }

    public void addComment(Comment comment){
        this.getComments().add(comment);
        comment.setMemo(this);
    }
}
