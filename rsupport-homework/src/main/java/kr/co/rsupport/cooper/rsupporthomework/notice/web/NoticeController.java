package kr.co.rsupport.cooper.rsupporthomework.notice.web;

import kr.co.rsupport.cooper.rsupporthomework.common.ApiResult;
import kr.co.rsupport.cooper.rsupporthomework.notice.application.NoticeService;
import kr.co.rsupport.cooper.rsupporthomework.notice.dto.AttachmentRequest;
import kr.co.rsupport.cooper.rsupporthomework.notice.dto.AttachmentResponse;
import kr.co.rsupport.cooper.rsupporthomework.notice.dto.NoticeRequest;
import kr.co.rsupport.cooper.rsupporthomework.notice.dto.NoticeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notices")
public class NoticeController {

    private final NoticeService noticeServiceImpl;

    @PostMapping
    public ResponseEntity<ApiResult<NoticeResponse>> createNotice(@RequestBody NoticeRequest noticeRequest) {
        NoticeResponse noticeResponse = noticeServiceImpl.createNotice(noticeRequest);
        ApiResult<NoticeResponse> apiResult = ApiResult.success(noticeResponse, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<NoticeResponse>> getNotice(@PathVariable Long id) {
        NoticeResponse noticeResponse = noticeServiceImpl.getNotice(id);
        ApiResult<NoticeResponse> apiResult = ApiResult.success(noticeResponse, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK)
                .body(apiResult);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<NoticeResponse>> updateNotice(@PathVariable Long id,
                                                                  @RequestBody NoticeRequest noticeRequest) {
        NoticeResponse noticeResponse = noticeServiceImpl.updateNotice(id, noticeRequest);
        ApiResult<NoticeResponse> apiResult = ApiResult.success(noticeResponse, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK)
                .body(apiResult);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeServiceImpl.deleteNotice(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{noticeId}/attachments/{attachmentId}")
    public ResponseEntity<ApiResult<AttachmentResponse>> updateAttachment(@PathVariable Long noticeId,
                                                                          @PathVariable Long attachmentId,
                                                                          @RequestBody AttachmentRequest request) {
        log.debug("{NoticeController.updateAttachment}");
        log.debug("noticeId : {}, attachId : {}, request : {}", noticeId, attachmentId, request);
        AttachmentResponse attachmentResponse = noticeServiceImpl.updateAttachment(noticeId, attachmentId, request);
        ApiResult<AttachmentResponse> apiResult = ApiResult.success(attachmentResponse, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK)
                .body(apiResult);
    }
}
