package core.contest5.member.service;


import core.contest5.admin.domain.SanctionType;
import core.contest5.admin.domain.SuspensionStatus;
import core.contest5.member.domain.UpdatedMemberInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberDomain{
    private Long id;
    private MemberInfo memberInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int warningCount;
    private SuspensionStatus status;
    private LocalDateTime suspensionEndTime;

    public void update(UpdatedMemberInfo updatedMemberInfo) {
        this.memberInfo = updatedMemberInfo.memberInfo();
    }

    public void increaseWarningCount() {
        warningCount++;
        if (status == SuspensionStatus.ACTIVE) {
            status = SuspensionStatus.WARNING;
        }
        applySuspensionBasedOnWarningCount();
    }

    private void applySuspensionBasedOnWarningCount() {
        switch (this.warningCount) {
            case 1:
                this.status = SuspensionStatus.WARNING;
                break;
            case 2:
                this.status = SuspensionStatus.SUSPENDED;
                this.suspensionEndTime = LocalDateTime.now().plusHours(24);
                break;
            case 3:
                this.status = SuspensionStatus.SUSPENDED;
                this.suspensionEndTime = LocalDateTime.now().plusDays(7);
                break;
            case 4:
                this.status = SuspensionStatus.BANNED;
                this.suspensionEndTime = null;
                break;
        }
    }

    public void applySuspension(SanctionType sanctionType) {
        switch (sanctionType) {
            case WARNING: // 있을 필요 있는지?
                this.status = SuspensionStatus.WARNING;
                break;
            case SUSPENSION_24H:
                this.status = SuspensionStatus.SUSPENDED;
                this.suspensionEndTime = LocalDateTime.now().plusHours(24);
                break;
            case SUSPENSION_7D:
                this.status = SuspensionStatus.SUSPENDED;
                this.suspensionEndTime = LocalDateTime.now().plusDays(7);
                break;
            case PERMANENT_BAN:
                this.status = SuspensionStatus.BANNED;
                this.suspensionEndTime = null;
                break;
        }
    }
}