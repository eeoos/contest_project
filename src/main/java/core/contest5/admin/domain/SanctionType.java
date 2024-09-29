package core.contest5.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SanctionType {
    WARNING("경고메시지"),
    SUSPENSION_24H("커뮤니티 활동 정지 24시간"),
    SUSPENSION_7D("커뮤니티 활동 정지 7일"),
    PERMANENT_BAN("영구정지");

    private final String message;
}
