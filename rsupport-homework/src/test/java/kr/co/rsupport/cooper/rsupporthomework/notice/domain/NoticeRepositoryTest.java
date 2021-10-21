package kr.co.rsupport.cooper.rsupporthomework.notice.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest
class NoticeRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    @DisplayName("공지 단일 조회 테스트 - 정상")
    void 단일_조회_정상() {
        //given
        String title = "제목1";
        String content = "내용1";
        String author = "작가1";
        LocalDateTime startTime = LocalDateTime.of(2021, 10, 20, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2021, 11, 20, 0, 0);

        Notice notice = Notice.builder()
                .title(title)
                .content(content)
                .author(author)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        //when
        testEntityManager.persist(notice);

        //then
        Notice findNotice = noticeRepository.findById(notice.getId()).get();
        assertThat(findNotice).isEqualTo(notice);
    }

    @Test
    @DisplayName("공지 조회 테스트 - 잘못된 id로 조회")
    void 올바르지_못한_id_조회() {
        //given
        String title = "제목1";
        String content = "내용1";
        String author = "작가1";
        LocalDateTime startTime = LocalDateTime.of(2021, 10, 20, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2021, 11, 20, 0, 0);

        Notice notice = Notice.builder()
                .title(title)
                .content(content)
                .author(author)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        //when
        testEntityManager.persist(notice);

        //then
        Long wrongId = 11L;
        assertThatThrownBy(() -> noticeRepository.findById(wrongId).get())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("공지 조회 목록 테스트 - 정상")
    void 조회_목록_정상() {
        //given
        Notice notice1 = Notice.builder()
                .title("제목1")
                .content("내용1")
                .author("작가1")
                .startTime(LocalDateTime.of(2021, 10, 20, 0, 0))
                .endTime(LocalDateTime.of(2021, 11, 20, 0, 0))
                .build();

        Notice notice2 = Notice.builder()
                .title("제목2")
                .content("내용2")
                .author("작가2")
                .startTime(LocalDateTime.of(2021, 10, 20, 0, 0))
                .endTime(LocalDateTime.of(2021, 11, 20, 0, 0))
                .build();

        Notice notice3 = Notice.builder()
                .title("제목3")
                .content("내용3")
                .author("작가3")
                .startTime(LocalDateTime.of(2021, 10, 20, 0, 0))
                .endTime(LocalDateTime.of(2021, 11, 20, 0, 0))
                .build();

        testEntityManager.persist(notice1);
        testEntityManager.persist(notice2);
        testEntityManager.persist(notice3);

        //when
        List<Notice> noticeList = noticeRepository.findAll();

        //then
        assertThat(noticeList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("공지 수정 테스트 - 정상")
    void 조회_수정_정상() {
        //given
        Notice notice = Notice.builder()
                .title("제목")
                .content("내용")
                .author("작가")
                .startTime(LocalDateTime.of(2021, 10, 20, 0, 0))
                .endTime(LocalDateTime.of(2021, 11, 20, 0, 0))
                .build();

        testEntityManager.persist(notice);

        notice.updateTitle("수정된 제목");
        notice.updateContent("수정된 내용");
        notice.updateAuthor("수정된 작가");
        notice.updateStartTime(LocalDateTime.of(2021, 10, 22, 0, 0));
        notice.updateEndTime(LocalDateTime.of(2021, 10, 24, 0, 0));

        //when
        noticeRepository.save(notice);

        //then
        assertThat(noticeRepository.findById(notice.getId()).get()).isEqualTo(notice);
    }

    @Test
    @DisplayName("공지 삭제 테스트 - 정상")
    void 조회_삭제_정상() {
        //given
        Notice notice = Notice.builder()
                .title("제목")
                .content("내용")
                .author("작가")
                .startTime(LocalDateTime.of(2021, 10, 20, 0, 0))
                .endTime(LocalDateTime.of(2021, 11, 20, 0, 0))
                .build();

        testEntityManager.persist(notice);

        //when
        noticeRepository.delete(notice);
        List<Notice> noticeList = noticeRepository.findAll();

        //then
        assertThat(noticeList.size()).isEqualTo(0);
    }
}