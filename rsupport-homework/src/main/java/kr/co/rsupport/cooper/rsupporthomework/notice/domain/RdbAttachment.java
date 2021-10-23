package kr.co.rsupport.cooper.rsupporthomework.notice.domain;

import kr.co.rsupport.cooper.rsupporthomework.notice.dto.AttachmentRequest;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RdbAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attchment_id")
    private Long id;

    @Column(name = "attchment_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "notice_id")
    private RdbNotice rdbNotice;

    @Builder
    public RdbAttachment(String name) {
        this.name = name;
    }

    public RedisAttachment toRedisEntity() {
        return new RedisAttachment(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RdbAttachment that = (RdbAttachment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void update(AttachmentRequest attachmentRequest) {
        this.name = attachmentRequest.getName();
    }
}
