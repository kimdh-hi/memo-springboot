package com.sparta.weeklytestspring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Builder
@AllArgsConstructor
@Data
public class ResponseDto {
    private String result;
    private String message;
    private HttpStatus httpStatus;
}
