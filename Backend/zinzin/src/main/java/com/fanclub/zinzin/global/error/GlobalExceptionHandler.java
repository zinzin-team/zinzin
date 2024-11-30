package com.fanclub.zinzin.global.error;

import com.fanclub.zinzin.global.error.code.CommonErrorCode;
import com.fanclub.zinzin.global.error.code.FileErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("Exception class:{}, message:{}", e.getClass(), e.getMessage());
        return ResponseEntity
                .status(FileErrorCode.FILE_MAX_UPLOAD_SIZE_EXCEEDED.getStatus())
                .body(ErrorResponse.of(FileErrorCode.FILE_MAX_UPLOAD_SIZE_EXCEEDED));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e){
        log.error("Exception code:{}, message:{}", e.getErrorCode(), e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception class:{}, message:{}", e.getClass(), e.getMessage());
        return ResponseEntity
                .status(CommonErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ErrorResponse.of(CommonErrorCode.INTERNAL_SERVER_ERROR));
    }
}
