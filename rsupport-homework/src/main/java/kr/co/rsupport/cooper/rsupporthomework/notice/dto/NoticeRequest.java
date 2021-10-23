package kr.co.rsupport.cooper.rsupporthomework.notice.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.rsupport.cooper.rsupporthomework.notice.domain.RdbAttachment;
import kr.co.rsupport.cooper.rsupporthomework.notice.domain.RdbNotice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeRequest {
    private String title;
    private String content;
    private String author;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<AttachmentRequest> attachments;

    public RdbNotice toRdbEntity() {
        List<RdbAttachment> rdbAttachments = toAttachments();
        return new RdbNotice(title, content, author, startTime, endTime);
    }

    public List<RdbAttachment> toAttachments() {
        return attachments.stream()
                .map(AttachmentRequest::toEntity)
                .collect(Collectors.toList());
    }
}
