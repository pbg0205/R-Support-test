package kr.co.rsupport.cooper.rsupporthomework.notice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.rsupport.cooper.rsupporthomework.notice.domain.RdbAttachment;
import lombok.*;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttachmentRequest {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    public RdbAttachment toEntity() {
        return RdbAttachment.builder()
                .name(name)
                .build();
    }
}
