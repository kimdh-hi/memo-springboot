package com.sparta.weeklytestspring.service;

import com.sparta.weeklytestspring.domain.Memo;
import com.sparta.weeklytestspring.domain.User;
import com.sparta.weeklytestspring.dto.MemoDto;
import com.sparta.weeklytestspring.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemoService {

    private final MemoRepository memoRepository;

    @Transactional(readOnly = true)
    public Page<Memo> getMyMemos(int page, int size, boolean isAsc, String field, User user, boolean isPublic) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, field);
        Pageable pageable = PageRequest.of(page, size, sort);

        if (!isPublic && user != null) {
            return memoRepository.findAllByUser(user.getId(), pageable);
        }

        return memoRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Memo> getMemos(int page, int size, boolean isAsc, String field) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, field);
        Pageable pageable = PageRequest.of(page, size, sort);

        return memoRepository.findAll(pageable);
    }


    @Transactional
    public Memo getMemo(Long memoId) {
        log.info("getMemo id = {}", memoId);
        Memo memo = memoRepository.findById(memoId).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 메모입니다.")
        );
        memo.increaseClickCount();

        return memo;
    }

    @Transactional
    public void saveMemo(MemoDto requestDto, User user) {
        Memo memo = Memo.builder()
                .title(requestDto.getTitle())
                .contents(requestDto.getContents())
                .clickCount(0L)
                .user(user)
                .build();

        memoRepository.save(memo);
    }

    @Transactional
    public void saveMemo(MemoDto requestDto, Boolean isAnonymous) {
        Memo memo = Memo.builder()
                .title(requestDto.getTitle())
                .contents(requestDto.getContents())
                .clickCount(0L)
                .isAnonymous(true)
                .build();

        memoRepository.save(memo);
    }

    @Transactional
    public void updateMemo(Long memoId, MemoDto requestDto) {
        Memo memo = findMemo(memoId);
        memo.updateMemo(requestDto);
    }

    @Transactional
    public void deleteMemo(Long memoId) {
        memoRepository.deleteById(memoId);
    }

    private Memo findMemo(Long memoId) {
        return memoRepository.findById(memoId).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 메모입니다.")
        );
    }
}
