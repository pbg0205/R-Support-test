package kr.co.rsupport.cooper.rsupporthomework.notice.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;


@DataRedisTest
class RedisNoticeRepositoryTest {

    @Autowired
    RedisNoticeRepository redisNoticeRepository;

    @Test
    @DisplayName("Redis 공지 저장 테스트 - 정상")
    void 공지_저장_정상() {
        //given
        Long id = 1L;
        String title = "제목1";
        String content = "내용1";
        String author = "작가1";
        LocalDateTime startTime = LocalDateTime.of(2021, 10, 20, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2021, 11, 20, 0, 0);

        RedisNotice redisNotice = RedisNotice.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        //when
        RedisNotice savedNotice = redisNoticeRepository.save(redisNotice);

        //then
        assertThat(savedNotice).isNotNull();
    }

    @Test
    @DisplayName("Redis 공지 조회 테스트 - 정상")
    void 공지_조회_정상() {
        //given
        Long id = 1L;
        String title = "제목1";
        String content = "내용1";
        String author = "작가1";
        LocalDateTime startTime = LocalDateTime.of(2021, 10, 20, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2021, 11, 20, 0, 0);

        RedisNotice redisNotice = RedisNotice.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        redisNoticeRepository.save(redisNotice);

        //when
        RedisNotice findNotice = redisNoticeRepository.findById(id).get();

        //then
        assertThat(findNotice.getId()).isEqualTo(id);
        assertThat(findNotice.getTitle()).isEqualTo(title);
        assertThat(findNotice.getContent()).isEqualTo(content);
        assertThat(findNotice.getAuthor()).isEqualTo(author);
        assertThat(findNotice.getStartTime()).isEqualTo(startTime);
        assertThat(findNotice.getEndTime()).isEqualTo(endTime);
    }

    @Test
    @DisplayName("Redis 공지 수정 테스트 - 정상")
    void 공지_수정_정상() {
        //given
        Long id = 1L;
        String title = "제목1";
        String content = "내용1";
        String author = "작가1";
        LocalDateTime startTime = LocalDateTime.of(2021, 10, 20, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2021, 11, 20, 0, 0);

        RedisNotice redisNotice = RedisNotice.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        redisNoticeRepository.save(redisNotice);

        String updateTitle = "수정된 제목";
        String updateContent = "수정된 내용";
        String updateAuthor = "수정된 작가";
        LocalDateTime updateStartTime = LocalDateTime.of(2021, 10, 22, 0, 0);
        LocalDateTime updateEndTime = LocalDateTime.of(2021, 10, 24, 0, 0);

        redisNotice.updateTitle(updateTitle);
        redisNotice.updateContent(updateContent);
        redisNotice.updateAuthor(updateAuthor);
        redisNotice.updateStartTime(updateStartTime);
        redisNotice.updateEndTime(updateEndTime);

        RedisNotice updatedNotice = redisNoticeRepository.save(redisNotice);

        //when
        RedisNotice findNotice = redisNoticeRepository.findById(id).get();

        //then
        assertThat(updatedNotice.getId()).isEqualTo(id);
        assertThat(updatedNotice.getTitle()).isEqualTo(updateTitle);
        assertThat(updatedNotice.getContent()).isEqualTo(updateContent);
        assertThat(updatedNotice.getAuthor()).isEqualTo(updateAuthor);
        assertThat(updatedNotice.getStartTime()).isEqualTo(updateStartTime);
        assertThat(updatedNotice.getEndTime()).isEqualTo(updateEndTime);
    }

    @Test
    @DisplayName("Redis 공지 삭제 테스트 - 정상")
    void 공지_삭제_정상() {
        //given
        Long id = 1L;
        String title = "제목1";
        String content = "내용1";
        String author = "작가1";
        LocalDateTime startTime = LocalDateTime.of(2021, 10, 20, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2021, 11, 20, 0, 0);

        RedisNotice redisNotice = RedisNotice.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        redisNoticeRepository.save(redisNotice);

        //when
        redisNoticeRepository.delete(redisNotice);

        //then
        assertThatThrownBy(() -> redisNoticeRepository.findById(id).get())
                .isInstanceOf(NoSuchElementException.class);
    }
}