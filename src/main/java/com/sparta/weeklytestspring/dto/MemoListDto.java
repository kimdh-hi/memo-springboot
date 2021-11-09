package com.sparta.weeklytestspring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class MemoListDto {

    private List<MemoDto> memos;

    private int page;
    private int size;
    private int totalPages;
}
