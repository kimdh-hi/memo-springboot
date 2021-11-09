package com.sparta.weeklytestspring.controller;

import com.sparta.weeklytestspring.dto.CommentDto;
import com.sparta.weeklytestspring.dto.ResponseDto;
import com.sparta.weeklytestspring.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/{memoId}")
    public ResponseDto addComment(@PathVariable Long memoId, @RequestBody CommentDto commentDto) {
        log.info("addComment memoId={}, comments={}", memoId, commentDto.getContents());
        commentService.saveComment(commentDto, memoId);
        ResponseDto responseDto = ResponseDto.builder()
                .message("댓글작성 완료")
                .result("success")
                .httpStatus(HttpStatus.OK).build();

        return responseDto;
    }
}
