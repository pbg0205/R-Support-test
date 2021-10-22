package kr.co.rsupport.cooper.rsupporthomework.notice.application;

import kr.co.rsupport.cooper.rsupporthomework.notice.domain.RdbNotice;
import kr.co.rsupport.cooper.rsupporthomework.notice.domain.RdbNoticeRepository;
import kr.co.rsupport.cooper.rsupporthomework.notice.domain.RedisNotice;
import kr.co.rsupport.cooper.rsupporthomework.notice.domain.RedisNoticeRepository;
import kr.co.rsupport.cooper.rsupporthomework.notice.dto.NoticeRequest;
import kr.co.rsupport.cooper.rsupporthomework.notice.dto.NoticeResponse;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.NotFoundNoticeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final RdbNoticeRepository rdbNoticeRepository;
    private final RedisNoticeRepository redisNoticeRepository;

    public NoticeResponse createNotice(NoticeRequest noticeRequest) {
        RdbNotice savedRdbNotice = rdbNoticeRepository.save(noticeRequest.toRdbEntity());
        RedisNotice savedRedisNotice = redisNoticeRepository.save(savedRdbNotice.toRedisEntity());
        return NoticeResponse.fromEntity(savedRedisNotice);
    }

    public NoticeResponse getNotice(Long id) {
        RedisNotice redisNotice;
        Optional<RedisNotice> redisNoticeOptional = redisNoticeRepository.findById(id);

        if (!redisNoticeOptional.isPresent()) {
            RdbNotice rdbNotice = rdbNoticeRepository.findById(id).orElseThrow(NotFoundNoticeException::new);
            redisNoticeRepository.save(rdbNotice.toRedisEntity());
            redisNoticeOptional = redisNoticeRepository.findById(id);
        }

        redisNotice = addNoticeViewCountAtRedis(redisNoticeOptional);

        if (redisNotice.isSynViewCount()) {
            synchronizeViewCountBetweenRedisAndRdb(id, redisNotice);
        }

        return NoticeResponse.fromEntity(redisNotice);
    }

    private void synchronizeViewCountBetweenRedisAndRdb(Long id, RedisNotice redisNotice) {
        RdbNotice rdbNotice = rdbNoticeRepository.findById(id).orElseThrow(NotFoundNoticeException::new);
        rdbNotice.updateViewCount(redisNotice.getViewCount());
        rdbNoticeRepository.save(rdbNotice);
    }

    private RedisNotice addNoticeViewCountAtRedis(Optional<RedisNotice> redisNoticeOptional) {
        RedisNotice redisNotice = redisNoticeOptional.get();
        redisNotice.addViewCount();
        redisNoticeRepository.save(redisNotice);
        return redisNotice;
    }

    public NoticeResponse updateNotice(Long id, NoticeRequest noticeRequest) {
        RdbNotice rdbNotice = rdbNoticeRepository.findById(id)
                .orElseThrow(NotFoundNoticeException::new);
        rdbNotice.update(noticeRequest);
        rdbNoticeRepository.save(rdbNotice);

        RedisNotice redisNotice = redisNoticeRepository.findById(id)
                .orElseThrow(NotFoundNoticeException::new);
        redisNotice.update(rdbNotice);
        redisNoticeRepository.save(redisNotice);

        return NoticeResponse.fromEntity(redisNotice);
    }

    @Transactional
    public void deleteNotice(Long id) {
        RedisNotice redisNotice = redisNoticeRepository.findById(id)
                .orElseThrow(NotFoundNoticeException::new);
        redisNoticeRepository.delete(redisNotice);

        RdbNotice rdbNotice = rdbNoticeRepository.findById(id)
                .orElseThrow(NotFoundNoticeException::new);
        rdbNoticeRepository.delete(rdbNotice);
    }
}
