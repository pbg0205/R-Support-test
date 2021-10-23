package kr.co.rsupport.cooper.rsupporthomework.notice.exception;

public class NotFoundAttachmentException extends RuntimeException {

    private static String NOT_FOUND_ATTACHMENT_MESSAGE = "원하는 첨부파일이 존재하지 않습니다";

    public NotFoundAttachmentException() {
        super(NOT_FOUND_ATTACHMENT_MESSAGE);
    }
}
