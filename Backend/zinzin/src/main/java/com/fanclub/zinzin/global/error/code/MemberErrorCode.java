package com.fanclub.zinzin.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode{
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "유저를 찾을 수 없습니다."),
    USER_EMAIL_DUPLICATE(HttpStatus.CONFLICT, "M002", "사용 중인 이메일 입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
