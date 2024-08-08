package com.fanclub.zinzin.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ChatRoomErrorCode implements ErrorCode{
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CH001", "채팅방을 찾을 수 없습니다."),
    CHAT_ROOM_CANNOT_DELETE(HttpStatus.UNAUTHORIZED, "CH002", "채팅방 삭제 권한이 없습니다."),
    CANNOT_GET_MESSAGES(HttpStatus.UNAUTHORIZED, "CH003", "채팅 내역 조회 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
