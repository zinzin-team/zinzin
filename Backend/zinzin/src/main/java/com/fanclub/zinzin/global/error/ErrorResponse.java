package com.fanclub.zinzin.global.error;

import com.fanclub.zinzin.global.error.code.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String code;
    private final String message;

    @Builder
    public ErrorResponse(String code, String message){
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse of(ErrorCode errorCode){
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }
}
