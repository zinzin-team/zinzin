package com.fanclub.zinzin.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TokenErrorCode implements ErrorCode{
    TOKEN_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "T001", "토큰을 생성할 수 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "T002", "유효하지 않은 입력입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
