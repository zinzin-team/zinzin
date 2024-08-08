package com.fanclub.zinzin.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MatchingErrorCode implements ErrorCode{
    INVALID_MATCHING_CARD(HttpStatus.BAD_REQUEST, "Z001", "매칭된 카드가 아닙니다."),
    CHECKED_CARD(HttpStatus.BAD_REQUEST, "Z002", "이미 확인한 카드입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
