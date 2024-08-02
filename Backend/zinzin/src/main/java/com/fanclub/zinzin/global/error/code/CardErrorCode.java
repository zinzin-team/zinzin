package com.fanclub.zinzin.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CardErrorCode implements ErrorCode {

    CARD_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "D001", "이미 생성된 카드가 있습니다."),
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "D002", "해당 태그를 찾을 수 없습니다."),
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "D003", "카드를 찾을 수 없습니다."),
    AUTHOR_MISMATCH(HttpStatus.BAD_REQUEST, "D004", "해당 카드의 작성자가 아닙니다."),
    INVALID_NUMBER_OF_IMAGES(HttpStatus.BAD_REQUEST, "D005", "업로드한 이미지가 3개보다 많거나 적습니다."),
    INVALID_NUMBER_OF_TAGS(HttpStatus.BAD_REQUEST, "D006", "선택한 태그가 5개보다 많거나 적습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
