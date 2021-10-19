package kr.co.rsupport.cooper.rsupporthomework.notice.exception;

public class InvalidTitleException extends RuntimeException {
    public InvalidTitleException(String message) {
        super(message);
    }

    public InvalidTitleException(String message, Throwable cause) {
        super(message, cause);
    }
}
