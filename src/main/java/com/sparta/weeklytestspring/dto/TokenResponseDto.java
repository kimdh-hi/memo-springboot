package com.sparta.weeklytestspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TokenResponseDto {
    private String token;
    private String message;
}
