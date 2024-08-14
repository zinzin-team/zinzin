package com.fanclub.zinzin.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FileErrorCode implements ErrorCode {
    FILE_UPLOAD_CONVERT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "L001", "파일 업로드 중 오류가 발생했습니다."),
    FILE_UPLOAD_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "L002", "파일 업로드 중 오류가 발생했습니다."),
    FILE_UPLOAD_WRITE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "L003", "파일 업로드 중 오류가 발생했습니다."),
    FILE_MAX_UPLOAD_SIZE_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "L004", "파일의 용량이 업로드 가능한 최대 용량을 초과했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
