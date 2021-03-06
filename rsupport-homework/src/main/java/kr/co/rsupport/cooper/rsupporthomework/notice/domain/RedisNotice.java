package kr.co.rsupport.cooper.rsupporthomework.notice.domain;

import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidAuthorException;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidContentException;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidNoticeTimeException;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidTitleException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
@RedisHash(("Notice"))
public class RedisNotice {

    @Id
    private Long id;

    private String title;

    private String content;

    private String author;

    private int viewCount = 0;

    @DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm")
    private LocalDateTime endTime;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm")
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm")
    private LocalDateTime lastModifiedAt;

    private List<RedisAttachment> attachments = new ArrayList<>();

    @Builder
    public RedisNotice(Long id,
                       String title,
                       String content,
                       String author,
                       LocalDateTime startTime,
                       LocalDateTime endTime) {
        validate(title, content, author, startTime, endTime);
        this.id = id;
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
            throw new InvalidTitleException("????????? ???????????? ???????????????.");
        }

    }

    private void validateContent(String content) {
        if (isNullAndBlank(content)) {
            throw new InvalidContentException("?????? ????????? ???????????? ????????????.");
        }
    }

    private void validateAuthor(String author) {
        if (isNullAndBlank(author)) {
            throw new InvalidAuthorException("???????????? ???????????? ???????????????.");
        }
    }

    private boolean isNullAndBlank(String value) {
        return Objects.isNull(value) || value.isEmpty();
    }

    private void validateNoticeTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new InvalidNoticeTimeException("?????? ???????????? ?????? ??????????????? ???????????????.");
        }
    }

    public void update(RdbNotice rdbNotice) {
        updateTitle(rdbNotice.getTitle());
        updateContent(rdbNotice.getContent());
        updateAuthor(rdbNotice.getAuthor());
        updateStartTime(rdbNotice.getStartTime());
        updateEndTime(rdbNotice.getEndTime());
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

    public void addAttachments(RedisAttachment redisAttachment) {
        attachments.add(redisAttachment);
    }

    public void addViewCount() {
        this.viewCount++;
    }

    public boolean isSynViewCount() {
        return viewCount % 10 == 0;
    }
}
