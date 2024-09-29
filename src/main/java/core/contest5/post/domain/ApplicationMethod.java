package core.contest5.post.domain;

import lombok.Getter;

@Getter
public enum ApplicationMethod {
    EMAIL("이메일 지원"),

//    QR("QR 코드로 지원"),

    HOMEPAGE("홈페이지로 지원");

    private final String description;

    ApplicationMethod(String description) {
        this.description = description;
    }
}