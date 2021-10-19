package kr.co.rsupport.cooper.rsupporthomework.notice.domain;

import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidAuthorException;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidContentException;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidNoticeTimeException;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.InvalidTitleException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;


class NoticeTest {

    @DisplayName("공지 생성 테스트 - 정상")
    @Test
    void 생성_정상() {
        assertThatCode(() -> Notice.builder()
                .title("제목1")
                .content("내용1")
                .author("작성자")
                .startTime(LocalDateTime.of(2021, 10, 11, 0, 0))
                .endTime(LocalDateTime.of(2021, 11, 13, 11, 0))
                .build()
        ).doesNotThrowAnyException();
    }

    @DisplayName("공지 생성 테스트 - 이름이 null")
    @Test
    void 생성_이름_null() {
        assertThatThrownBy(() -> Notice.builder()
                .title(null)
                .content("내용1")
                .author("작성자")
                .startTime(LocalDateTime.of(2021, 10, 11, 0, 0))
                .endTime(LocalDateTime.of(2021, 11, 13, 11, 0))
                .build()
        ).isInstanceOf(InvalidTitleException.class);
    }

    @DisplayName("공지 생성 테스트 - 내용이 null")
    @Test
    void 생성_내용_null() {
        assertThatThrownBy(() -> Notice.builder()
                .title("제목1")
                .content(null)
                .author("작성자")
                .startTime(LocalDateTime.of(2021, 10, 11, 0, 0))
                .endTime(LocalDateTime.of(2021, 11, 13, 11, 0))
                .build()
        ).isInstanceOf(InvalidContentException.class);
    }

    @DisplayName("공지 생성 테스트 - 작성자가 null")
    @Test
    void 생성_작성자_null() {
        assertThatThrownBy(() -> Notice.builder()
                .title("제목1")
                .content("내용1")
                .author(null)
                .startTime(LocalDateTime.of(2021, 10, 11, 0, 0))
                .endTime(LocalDateTime.of(2021, 11, 13, 11, 0))
                .build()
        ).isInstanceOf(InvalidAuthorException.class);
    }

    @DisplayName("공지 생성 테스트 - 공지 시작일자 > 공지 종료일자")
    @Test
    void 생성_공지_시작일자가_종료일자보다_앞선다() {
        assertThatThrownBy(() -> Notice.builder()
                .title("제목1")
                .content("내용1")
                .author("작성자")
                .startTime(LocalDateTime.of(2021, 12, 11, 0, 0))
                .endTime(LocalDateTime.of(2021, 11, 13, 11, 0))
                .build()
        ).isInstanceOf(InvalidNoticeTimeException.class);
    }
}