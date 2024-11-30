package com.fanclub.zinzin.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode{
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "인증 토큰이 올바르지 않습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "A002", "권한이 없습니다."),
    OAUTH2_PROVIDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "OA001", "해당 Provider는 지원되지 않습니다."),
    OAUTH2_AUTHENTICATION_FAILED(HttpStatus.BAD_REQUEST, "OA002", "OAUTH2 사용자 정보를 가져오는 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
