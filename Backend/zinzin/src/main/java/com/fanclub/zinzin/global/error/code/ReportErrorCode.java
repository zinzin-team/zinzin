package com.fanclub.zinzin.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReportErrorCode implements ErrorCode {
    MISMATCH_REPORTED_CARD_MEMBER(HttpStatus.BAD_REQUEST, "X001", "신고할 카드와 유저 정보가 일치하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}