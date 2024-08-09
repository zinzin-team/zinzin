package com.fanclub.zinzin.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RewardErrorCode implements ErrorCode {

    NOT_MEMBER_OF_CHATROOM(HttpStatus.FORBIDDEN, "R001", "해당 채팅방의 구성원이 아닙니다."),
    ALREADY_SELECTED(HttpStatus.BAD_REQUEST, "R002", "이미 주선자를 선택하였습니다."),
    DUPLICATED(HttpStatus.BAD_REQUEST, "R003", "중복된 데이터가 들어갔습니다. DB 확인이 필요합니다."),
    SELF_SELECTED(HttpStatus.BAD_REQUEST, "R004", "본인을 선택할 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
