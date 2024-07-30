package com.fanclub.zinzin.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FollowErrorCode implements ErrorCode{
    FOLLOW_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "F001", "지인 관계 설정에 이용될 회원 정보가 존재하지 않습니다."),
    FOLLOW_RELATION_EXIST(HttpStatus.BAD_REQUEST, "F002", "지인 관련 관계가 이미 존재합니다."),
    FOLLOW_REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST, "F003", "응답할 지인 요청이 존재하지 않습니다."),
    ACCEPT_FOLLOW_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "F004", "지인 요청 수락 처리 중 오류가 발생했습니다."),
    REJECT_FOLLOW_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "F005", "지인 요청 거절 처리 중 오류가 발생했습니다."),
    INVALID_FOLLOW_RELATION(HttpStatus.BAD_REQUEST, "F006", "지인 관계 끊기가 불가능한 지인 관계입니다."),
    UNFOLLOW_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "F007", "지인 관계 끊기 처리 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
