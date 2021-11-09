package com.sparta.weeklytestspring.service;

import com.sparta.weeklytestspring.domain.Comment;
import com.sparta.weeklytestspring.domain.Memo;
import com.sparta.weeklytestspring.dto.CommentDto;
import com.sparta.weeklytestspring.repository.CommentRepository;
import com.sparta.weeklytestspring.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemoRepository memoRepository;

    @Transactional
    public void saveComment(CommentDto commentDto, Long memoId) {
        Memo memo = memoRepository.findById(memoId).get();
        Comment comment = Comment.builder().contents(commentDto.getContents()).build();
        comment.setMemo(memo);

        commentRepository.save(comment);
    }

    public List<Comment> getComments(Long memoId) {
        return commentRepository.findByMemoId(memoId);
    }
}
