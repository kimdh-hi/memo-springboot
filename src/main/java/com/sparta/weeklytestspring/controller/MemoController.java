package com.sparta.weeklytestspring.controller;

import com.sparta.weeklytestspring.domain.Comment;
import com.sparta.weeklytestspring.domain.Memo;
import com.sparta.weeklytestspring.domain.User;
import com.sparta.weeklytestspring.dto.CommentDto;
import com.sparta.weeklytestspring.dto.MemoDto;
import com.sparta.weeklytestspring.dto.MemoListDto;
import com.sparta.weeklytestspring.security.UserDetailsImpl;
import com.sparta.weeklytestspring.service.CommentService;
import com.sparta.weeklytestspring.service.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@RestController
public class MemoController {

    private final MemoService memoService;
    private final CommentService commentService;
    
    @PostMapping("/memo")
    public ResponseEntity<String> saveMemo(
            @RequestBody MemoDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails != null) {
            User user = (User) userDetails.getUser();
            memoService.saveMemo(requestDto, user);
        } else {
            memoService.saveMemo(requestDto, true);
        }
        return ResponseEntity.ok("메모작성 완료");
    }

    @GetMapping("/memos")
    public MemoListDto findMemos(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam boolean isAsc,
            @RequestParam String field,
            @RequestParam boolean isPublic,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("memos page = {}, size = {}, isAsc = {}, type = {}", page, size, isAsc, field);

        page--;

        Page<Memo> memos;

        if (userDetails != null && !isPublic) {
            memos = memoService.getMyMemos(page, size, isAsc, field, userDetails.getUser(), isPublic);
        } else {
            memos = memoService.getMemos(page, size, isAsc, field);
        }

        List<MemoDto> memoDtos = memos.stream().map(m -> {
            return MemoDto.builder()
                    .id(m.getId())
                    .title(m.getTitle())
                    .contents(m.getContents())
                    .clickCount(m.getClickCount())
                    .username(m.getIsAnonymous() ? "비회원" : m.getUser().getUsername())
                    .createdAt(m.getCreatedAt()).build();
        }).collect(Collectors.toList());


        MemoListDto memoList = MemoListDto.builder()
                .memos(memoDtos)
                .page(memos.getNumber())
                .size(memos.getSize())
                .totalPages(memos.getTotalPages()).build();

        return memoList;

    }

    @GetMapping("/memo/{memoId}")
    public ResponseEntity<MemoDto> findMemo(@PathVariable Long memoId) {
        Memo memo = memoService.getMemo(memoId);


        List<String> comments = commentService.getComments(memoId).stream()
                .map(c -> String.valueOf(c.getContents())).collect(Collectors.toList());

        String username = memo.getUser() == null ? "비회원" : memo.getUser().getUsername();

        MemoDto memoDto = MemoDto.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .contents(memo.getContents())
                .clickCount(memo.getClickCount())
                .username(username)
                .comments(comments)
                .createdAt(memo.getCreatedAt()).build();

        return ResponseEntity.ok().body(memoDto);
    }


    @PutMapping("/memo/{memoId}")
    public ResponseEntity<String> updateMemo(@PathVariable Long memoId, @RequestBody MemoDto memoDto) {
        memoService.updateMemo(memoId, memoDto);

        return ResponseEntity.ok().body("success");
    }


    @DeleteMapping("/memo/{memoId}")
    public ResponseEntity<String> deleteMemo(@PathVariable Long memoId) {
        memoService.deleteMemo(memoId);

        return ResponseEntity.ok().body("success");
    }
}
