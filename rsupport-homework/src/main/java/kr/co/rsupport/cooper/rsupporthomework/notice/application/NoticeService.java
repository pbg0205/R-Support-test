package kr.co.rsupport.cooper.rsupporthomework.notice.application;

import kr.co.rsupport.cooper.rsupporthomework.notice.dto.NoticeRequest;
import kr.co.rsupport.cooper.rsupporthomework.notice.dto.NoticeResponse;

public interface NoticeService {
    NoticeResponse createNotice(NoticeRequest noticeRequest);
    NoticeResponse getNotice(Long id);
    NoticeResponse updateNotice(Long id, NoticeRequest noticeRequest);
    void deleteNotice(Long id);
}
