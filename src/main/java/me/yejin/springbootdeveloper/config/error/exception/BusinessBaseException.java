package me.yejin.springbootdeveloper.config.error.exception;

import me.yejin.springbootdeveloper.config.error.ErrorCode;

//비즈니스 로직 관련 예외를 모아놓을 최상위 클래스
public class BusinessBaseException  extends RuntimeException{

    private final ErrorCode errorCode;

    public BusinessBaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessBaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
