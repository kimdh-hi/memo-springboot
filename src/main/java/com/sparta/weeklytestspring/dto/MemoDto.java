package com.sparta.weeklytestspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MemoDto {

    private String title;

    private String contents;

    private Long clickCount;

    private String username;

}
