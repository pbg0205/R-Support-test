package kr.co.rsupport.cooper.rsupporthomework.notice.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RdbNoticeRepository extends JpaRepository<RdbNotice, Long> {
    @Query("SELECT rdb FROM RdbNotice rdb JOIN FETCH rdb.rdbAttachments WHERE rdb.id = :id")
    Optional<RdbNotice> findByIdUsingJoin(@Param("id") Long id);
}
