package kr.co.rsupport.cooper.rsupporthomework.notice.web;

import kr.co.rsupport.cooper.rsupporthomework.common.ApiResult;
import kr.co.rsupport.cooper.rsupporthomework.notice.application.NoticeService;
import kr.co.rsupport.cooper.rsupporthomework.notice.dto.NoticeRequest;
import kr.co.rsupport.cooper.rsupporthomework.notice.dto.NoticeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notice")
public class NoticeController {

    private final NoticeService noticeServiceImpl;

    @PostMapping
    public ResponseEntity<ApiResult<NoticeResponse>> createNotice(@RequestBody NoticeRequest noticeRequest) {
        NoticeResponse noticeResponse = noticeServiceImpl.createNotice(noticeRequest);
        ApiResult<NoticeResponse> apiResult = ApiResult.success(noticeResponse, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResult);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResult<NoticeResponse>> getNotice(@PathVariable Long id) {
        NoticeResponse noticeResponse = noticeServiceImpl.getNotice(id);
        ApiResult<NoticeResponse> apiResult = ApiResult.success(noticeResponse, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK)
                .body(apiResult);
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResult<NoticeResponse>> updateNotice(@PathVariable Long id,
                                                                  @RequestBody NoticeRequest noticeRequest) {
        NoticeResponse noticeResponse = noticeServiceImpl.updateNotice(id, noticeRequest);
        ApiResult<NoticeResponse> apiResult = ApiResult.success(noticeResponse, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK)
                .body(apiResult);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeServiceImpl.deleteNotice(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
