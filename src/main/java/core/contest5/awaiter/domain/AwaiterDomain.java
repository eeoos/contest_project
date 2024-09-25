package core.contest5.awaiter.domain;

import java.time.LocalDateTime;

import core.contest5.member.service.MemberDomain;
import core.contest5.post.service.PostDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AwaiterDomain {
    private AwaiterId id;
    private PostDomain post; //?
    private MemberDomain member; //?
    private ApplicationStatus applicationStatus;

    public void updateApplicationStatus(ApplicationStatus newStatus) {
        this.applicationStatus = newStatus;
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

    public static AwaiterDomain createNewAwaiter(PostDomain post, MemberDomain member) {
        return AwaiterDomain.builder()
                .id(new AwaiterId(member.getId(), post.getId()))
                .post(post)
                .member(member)
                .applicationStatus(ApplicationStatus.PENDING)
                .build();
    }
}