package core.contest5.awaiter.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AwaiterDomain {

    private Long id;
    private Long postId;
    private Long memberId;
    private LocalDateTime registrationDate;
    private String profileDescription;
    private ApplicationStatus applicationStatus;

    public void updateApplicationStatus(ApplicationStatus newStatus) {
        this.applicationStatus = newStatus;
    }

    public void updateProfileDescription(String newDescription) {
        this.profileDescription = newDescription;
    }

    public boolean isPending() {
        return this.applicationStatus == ApplicationStatus.PENDING;
    }

    public boolean isAccepted() {
        return this.applicationStatus == ApplicationStatus.ACCEPTED;
    }

    public boolean isRejected() {
        return this.applicationStatus == ApplicationStatus.REJECTED;
    }

    public void validateStatusTransition(ApplicationStatus newStatus) {
        if (this.applicationStatus == ApplicationStatus.ACCEPTED || this.applicationStatus == ApplicationStatus.REJECTED) {
            throw new IllegalStateException("Cannot change status once it's been accepted or rejected");
        }
        if (this.applicationStatus == newStatus) {
            throw new IllegalStateException("New status must be different from current status");
        }
    }

    public static AwaiterDomain createNewAwaiter(Long postId, Long memberId, String profileDescription) {
        return AwaiterDomain.builder()
                .postId(postId)
                .memberId(memberId)
                .registrationDate(LocalDateTime.now())
                .profileDescription(profileDescription)
                .applicationStatus(ApplicationStatus.PENDING)
                .build();
    }
}