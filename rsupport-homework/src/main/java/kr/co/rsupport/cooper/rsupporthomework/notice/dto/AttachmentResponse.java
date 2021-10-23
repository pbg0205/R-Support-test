package kr.co.rsupport.cooper.rsupporthomework.notice.dto;


import kr.co.rsupport.cooper.rsupporthomework.notice.domain.RedisAttachment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttachmentResponse {
    private Long id;
    private String name;

    public static AttachmentResponse from(RedisAttachment redisAttachment) {
        return new AttachmentResponse(redisAttachment.getId(), redisAttachment.getName());
    }
}
