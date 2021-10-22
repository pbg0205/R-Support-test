package kr.co.rsupport.cooper.rsupporthomework.notice.dto;


import kr.co.rsupport.cooper.rsupporthomework.notice.domain.RdbNotice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeRequest {
    private String title;
    private String content;
    private String author;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public RdbNotice toRdbEntity() {
        return RdbNotice.builder()
                .title(title)
                .content(content)
                .author(author)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
