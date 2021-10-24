package kr.co.rsupport.cooper.rsupporthomework.notice.application;

import kr.co.rsupport.cooper.rsupporthomework.notice.domain.*;
import kr.co.rsupport.cooper.rsupporthomework.notice.dto.AttachmentRequest;
import kr.co.rsupport.cooper.rsupporthomework.notice.dto.AttachmentResponse;
import kr.co.rsupport.cooper.rsupporthomework.notice.dto.NoticeRequest;
import kr.co.rsupport.cooper.rsupporthomework.notice.dto.NoticeResponse;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.NotFoundAttachmentException;
import kr.co.rsupport.cooper.rsupporthomework.notice.exception.NotFoundNoticeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final RdbNoticeRepository rdbNoticeRepository;
    private final RedisNoticeRepository redisNoticeRepository;

    @Transactional
    public NoticeResponse createNotice(NoticeRequest noticeRequest) {
        RdbNotice rdbNotice = noticeRequest.toRdbEntity();
        addRdbAttachments(noticeRequest, rdbNotice);
        RdbNotice savedRdbNotice = rdbNoticeRepository.save(rdbNotice);

        RedisNotice redisNotice = savedRdbNotice.toRedisEntity();
        addRedisAttachments(rdbNotice, redisNotice);
        RedisNotice savedRedisNotice = redisNoticeRepository.save(redisNotice);

        return NoticeResponse.fromEntity(savedRedisNotice);
    }

    private void addRdbAttachments(NoticeRequest noticeRequest, RdbNotice rdbNotice) {
        noticeRequest.getAttachments().stream()
                .map(AttachmentRequest::toEntity)
                .forEach(rdbNotice::addAttachment);
    }

    private void addRedisAttachments(RdbNotice rdbNotice, RedisNotice redisNotice) {
        rdbNotice.getRdbAttachments().stream()
                .map(RdbAttachment::toRedisEntity)
                .forEach(redisNotice::addAttachments);
    }

    @Transactional
    public NoticeResponse getNotice(Long id) {
        RedisNotice redisNotice;
        Optional<RedisNotice> redisNoticeOptional = redisNoticeRepository.findById(id);

        if (!redisNoticeOptional.isPresent()) {
            RdbNotice rdbNotice = rdbNoticeRepository.findByIdUsingJoin(id).orElseThrow(NotFoundNoticeException::new);
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
        RdbNotice rdbNotice = rdbNoticeRepository.findByIdUsingJoin(id).orElseThrow(NotFoundNoticeException::new);
        rdbNotice.updateViewCount(redisNotice.getViewCount());
        rdbNoticeRepository.save(rdbNotice);
    }

    private RedisNotice addNoticeViewCountAtRedis(Optional<RedisNotice> redisNoticeOptional) {
        RedisNotice redisNotice = redisNoticeOptional.get();
        redisNotice.addViewCount();
        redisNoticeRepository.save(redisNotice);
        return redisNotice;
    }

    @Transactional
    public NoticeResponse updateNotice(Long id, NoticeRequest noticeRequest) {
        RdbNotice rdbNotice = rdbNoticeRepository.findByIdUsingJoin(id)
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

    @Transactional
    public AttachmentResponse updateAttachment(Long noticeId, Long attachmentId, AttachmentRequest attachmentRequest) {
        RdbNotice rdbNotice = rdbNoticeRepository.findByIdUsingJoin(noticeId).orElseThrow(NotFoundNoticeException::new);
        RdbAttachment rdbAttachment = rdbNotice.getRdbAttachments().stream()
                .filter(attachment -> attachment.getId().equals(attachmentId))
                .findFirst()
                .orElseThrow(NotFoundAttachmentException::new);
        rdbAttachment.update(attachmentRequest);
        rdbNoticeRepository.save(rdbNotice);

        RedisNotice redisNotice = redisNoticeRepository.findById(noticeId)
                .orElseThrow(NotFoundNoticeException::new);
        RedisAttachment redisAttachment = redisNotice.getAttachments().stream()
                .filter(attachment -> attachment.getId().equals(attachmentId))
                .findFirst()
                .orElseThrow(NotFoundAttachmentException::new);
        redisAttachment.update(rdbAttachment);
        redisNoticeRepository.save(redisNotice);

        log.debug("redisAttachment : {}", redisAttachment);

        return AttachmentResponse.from(redisAttachment);
    }
}
