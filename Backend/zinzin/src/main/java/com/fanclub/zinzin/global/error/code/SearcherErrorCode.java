package com.fanclub.zinzin.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SearcherErrorCode implements ErrorCode{
    INVALID_SEARCH_ID(HttpStatus.BAD_REQUEST, "S001", "유효하지 않은 검색용 아이디입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
