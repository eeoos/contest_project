package core.contest5.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuspensionStatus {

    ACTIVE,
    WARNING,
    SUSPENDED,
    BANNED
}
