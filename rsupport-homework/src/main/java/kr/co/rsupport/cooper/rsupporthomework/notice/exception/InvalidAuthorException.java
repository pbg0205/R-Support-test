package kr.co.rsupport.cooper.rsupporthomework.notice.exception;

public class InvalidAuthorException extends RuntimeException {
    public InvalidAuthorException(String message) {
        super(message);
    }

    public InvalidAuthorException(String message, Throwable cause) {
        super(message, cause);
    }
}
