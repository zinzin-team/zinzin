package com.fanclub.zinzin.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TokenErrorCode implements ErrorCode{
    TOKEN_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "T001", "토큰을 생성할 수 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "T002", "유효하지 않은 입력입니다."),
    TOKEN_DECODE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "T003", "토큰 정보 추출에 실패했습니다."),
    ID_TOKEN_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "T004", "아이디 토큰 형식이 유효하지 않습니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED,"T005","토큰이 유효하지 않거나 없습니다."),
    TOKEN_CANNOT_REFRESH(HttpStatus.UNAUTHORIZED,"T006","리프레시 토큰이 유효하지 않거나 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
