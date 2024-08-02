package com.fanclub.zinzin.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum KakaoErrorCode implements ErrorCode {
    KAKAO_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "K000", "카카오 API 요청에 실패했습니다."),
    CANNOT_GET_KAKAO_FRIENDS(HttpStatus.NOT_FOUND, "K001", "카카오 친구 목록을 가져올 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}