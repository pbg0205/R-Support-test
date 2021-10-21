package kr.co.rsupport.cooper.rsupporthomework.notice.domain;

import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidAuthorException;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidContentException;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidNoticeTimeException;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidTitleException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@RedisHash(("Notice"))
public class RedisNotice {

    @Id
    private Long id;

    private String title;

    private String content;

    private String author;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastModifiedAt;

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
}
