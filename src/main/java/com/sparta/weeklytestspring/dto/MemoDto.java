package com.sparta.weeklytestspring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

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

}
