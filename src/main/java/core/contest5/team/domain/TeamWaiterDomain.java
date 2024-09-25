package core.contest5.team.domain;

import core.contest5.awaiter.domain.ApplicationStatus;
import core.contest5.post.service.PostDomain;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeamWaiterDomain {

    private TeamWaiterId id;
    private PostDomain post;
    private TeamDomain team;
    private LocalDateTime registrationDate;
    private ApplicationStatus applicationStatus;

    public void updateApplicationStatus(ApplicationStatus newStatus) {
        this.applicationStatus = newStatus;
    }
}
