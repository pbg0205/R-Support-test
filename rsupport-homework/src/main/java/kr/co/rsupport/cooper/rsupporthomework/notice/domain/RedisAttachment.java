package kr.co.rsupport.cooper.rsupporthomework.notice.domain;

import lombok.Getter;

@Getter
public class RedisAttachment {
    private Long id;
    private String name;

    public RedisAttachment(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void update(RdbAttachment rdbAttachment) {
        this.name = rdbAttachment.getName();
    }
}
