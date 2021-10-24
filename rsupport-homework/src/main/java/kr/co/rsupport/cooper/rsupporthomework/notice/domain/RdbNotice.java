package kr.co.rsupport.cooper.rsupporthomework.notice.domain;

import kr.co.rsupport.cooper.rsupporthomework.common.BaseEntity;
import kr.co.rsupport.cooper.rsupporthomework.notice.dto.NoticeRequest;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidAuthorException;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidContentException;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidNoticeTimeException;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidTitleException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RdbNotice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @Column(name = "notice_title", nullable = false)
    private String title;

    @Column(name = "notice_content")
    private String content;

    @Column(name = "notice_author", nullable = false)
    private String author;

    @Column(name = "notice_view_count")
    private int viewCount = 0;

    @Column(name = "notice_start_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @Column(name = "notice_end_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "rdbNotice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RdbAttachment> rdbAttachments = new ArrayList<>();

    @Builder
    public RdbNotice(String title,
                     String content,
                     String author,
                     LocalDateTime startTime,
                     LocalDateTime endTime) {
        validate(title, content, author, startTime, endTime);
        this.title = title;
        this.content = content;
        this.author = author;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void validate(String title, String content, String author, LocalDateTime startTime, LocalDateTime endTime) {
        validateTitle(title);
        validateContent(content);
        validateAuthor(author);
        validateNoticeTime(startTime, endTime);
    }

    private void validateTitle(String title) {
        if (isNullAndBlank(title)) {
            throw new InvalidTitleException("제목이 존재하지 않았습니다.");
        }

    }

    private void validateContent(String content) {
        if (isNullAndBlank(content)) {
            throw new InvalidContentException("공지 내용이 존재하지 않습니다.");
        }
    }

    private void validateAuthor(String author) {
        if (isNullAndBlank(author)) {
            throw new InvalidAuthorException("사용자를 입력하지 않았습니다.");
        }
    }

    private boolean isNullAndBlank(String value) {
        return Objects.isNull(value) || value.isEmpty();
    }

    private void validateNoticeTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new InvalidNoticeTimeException("공지 시작일이 공지 종료일보다 이후입니다.");
        }
    }

    public void update(NoticeRequest noticeRequest) {
        updateTitle(noticeRequest.getTitle());
        updateContent(noticeRequest.getContent());
        updateAuthor(noticeRequest.getAuthor());
        updateStartTime(noticeRequest.getStartTime());
        updateEndTime(noticeRequest.getEndTime());
    }

    public void updateTitle(String title) {
        validateTitle(title);
        this.title = title;
    }

    public void updateContent(String content) {
        validateContent(content);
        this.content = content;
    }

    public void updateAuthor(String author) {
        validateAuthor(author);
        this.author = author;
    }

    public void updateStartTime(LocalDateTime startTime) {
        validateNoticeTime(startTime, endTime);
        this.startTime = startTime;
    }

    public void updateEndTime(LocalDateTime endTime) {
        validateNoticeTime(startTime, endTime);
        this.endTime = endTime;
    }

    public void updateViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public RedisNotice toRedisEntity() {
        return RedisNotice.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public List<RedisAttachment> toAttachmentsRedisEntity() {
        return rdbAttachments.stream().map(RdbAttachment::toRedisEntity)
                .collect(Collectors.toList());
    }

    public void addAttachment(RdbAttachment rdbAttachment) {
        rdbAttachments.add(rdbAttachment);
        rdbAttachment.setRdbNotice(this);
    }
}

