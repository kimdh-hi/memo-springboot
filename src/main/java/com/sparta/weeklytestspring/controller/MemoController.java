package com.sparta.weeklytestspring.controller;

import com.sparta.weeklytestspring.domain.Memo;
import com.sparta.weeklytestspring.domain.User;
import com.sparta.weeklytestspring.dto.MemoDto;
import com.sparta.weeklytestspring.security.UserDetailsImpl;
import com.sparta.weeklytestspring.service.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
public class MemoController {

    private final MemoService memoService;
    
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
    public ResponseEntity<Page<Memo>> findMemos(
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
        log.info("memos = {}", memos);
        return ResponseEntity.ok().body(memos);
    }

    @GetMapping("/memo/{memoId}")
    public ResponseEntity<MemoDto> findMemo(@PathVariable Long memoId) {
        Memo memo = memoService.getMemo(memoId);

        String username = memo.getUser() == null ? "비회원" : memo.getUser().getUsername();

        MemoDto memoDto = MemoDto.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .contents(memo.getContents())
                .clickCount(memo.getClickCount())
                .username(username)
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
