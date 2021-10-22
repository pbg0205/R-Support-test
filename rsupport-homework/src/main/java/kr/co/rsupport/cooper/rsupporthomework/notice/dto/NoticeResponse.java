package kr.co.rsupport.cooper.rsupporthomework.notice.dto;


import kr.co.rsupport.cooper.rsupporthomework.notice.domain.RdbNotice;
import kr.co.rsupport.cooper.rsupporthomework.notice.domain.RedisNotice;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private int viewCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static NoticeResponse fromEntity(RedisNotice notice) {
        return new NoticeResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getAuthor(),
                notice.getViewCount(),
                notice.getStartTime(),
                notice.getEndTime()
        );
    }
}
