package com.sparta.weeklytestspring.controller;

import com.sparta.weeklytestspring.domain.Memo;
import com.sparta.weeklytestspring.domain.User;
import com.sparta.weeklytestspring.dto.MemoDto;
import com.sparta.weeklytestspring.security.UserDetailsImpl;
import com.sparta.weeklytestspring.service.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
public class MemoController {

    private final MemoService memoService;

    @Secured("ROLE_USER")
    @PostMapping("/memo")
    public ResponseEntity<String> saveMemo(@RequestBody MemoDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = (User) userDetails.getUser();
        memoService.saveMemo(requestDto, user);
        return ResponseEntity.ok("메모작성 완료");
    }

    @GetMapping("/memos")
    public ResponseEntity<Page<Memo>> findMemos(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam boolean isAsc,
            @RequestParam String type,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("memos page = {}, size = {}, isAsc = {}, type = {}", page, size, isAsc, type);

        page--;

        Page<Memo> memos;

        if (userDetails == null) {
            memos = memoService.getMemos(page, size, isAsc, type);
        } else {
            memos = memoService.getMemos(page, size, isAsc, type, userDetails.getUser());
        }
        log.info("memos = {}", memos);
        return ResponseEntity.ok().body(memos);
    }

    @GetMapping("/memo")
    public ResponseEntity<MemoDto> findMemo(@RequestParam Long id) {
        Memo memo = memoService.getMemo(id);
        MemoDto memoDto = new MemoDto(memo.getTitle(), memo.getContents(), memo.getClickCount(), memo.getUser().getUsername());

        return ResponseEntity.ok().body(memoDto);
    }

    @Secured("ROLE_USER")
    @PutMapping("/memo/{memoId}")
    public ResponseEntity<String> updateMemo(@PathVariable Long memoId, @RequestBody MemoDto memoDto) {
        memoService.updateMemo(memoId, memoDto);

        return ResponseEntity.ok().body("success");
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/memo/{memoId}")
    public ResponseEntity<String> deleteMemo(@PathVariable Long memoId) {
        memoService.deleteMemo(memoId);

        return ResponseEntity.ok().body("success");
    }
}
