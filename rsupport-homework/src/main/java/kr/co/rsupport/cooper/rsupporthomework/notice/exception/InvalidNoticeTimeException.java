package kr.co.rsupport.cooper.rsupporthomework.notice.exception;

public class InvalidNoticeTimeException extends RuntimeException {
    public InvalidNoticeTimeException(String message) {
        super(message);
    }

    public InvalidNoticeTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
