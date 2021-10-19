package kr.co.rsupport.cooper.rsupporthomework.notice.domain;

import kr.co.rsupport.cooper.rsupporthomework.common.BaseEntity;
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
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

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

    @Column(name = "notice_start_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @Column(name = "notice_end_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @Builder
    public Notice(String title, String content, String author, LocalDateTime startTime, LocalDateTime endTime) {
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
}

