package com.sparta.weeklytestspring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class MemoDto {

    private Long id;
    private String title;
    private String contents;
    private Long clickCount;
    private String username;
    private LocalDateTime createdAt;
    private List<String> comments = new ArrayList<>();
}
