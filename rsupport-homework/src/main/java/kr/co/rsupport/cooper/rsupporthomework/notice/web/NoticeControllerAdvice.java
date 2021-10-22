package kr.co.rsupport.cooper.rsupporthomework.notice.web;

import kr.co.rsupport.cooper.rsupporthomework.common.ApiResult;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.NotFoundNoticeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NoticeControllerAdvice {

    @ExceptionHandler(NotFoundNoticeException.class)
    public ResponseEntity<ApiResult<Void>> notFoundNoticeExceptionHandler(NotFoundNoticeException exception) {
        ApiResult apiResult = ApiResult.fail(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(apiResult);
    }
}
