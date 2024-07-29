package com.fanclub.zinzin.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FollowErrorCode implements ErrorCode{
    FOLLOW_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "F001", "지인 요청할 회원 정보가 존재하지 않습니다."),
    FOLLOW_RELATION_EXIST(HttpStatus.BAD_REQUEST, "F002", "지인 관련 관계가 이미 존재합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
