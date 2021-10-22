package kr.co.rsupport.cooper.rsupporthomework.notice.exception;

public class NotFoundNoticeException extends RuntimeException {

    private static String NOT_FOUND_NOTICE_MESSAGE = "원하는 공지가 존재하지 않습니다";

    public NotFoundNoticeException() {
        super(NOT_FOUND_NOTICE_MESSAGE);
    }
}
