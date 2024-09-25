package core.contest5.team.domain;

import core.contest5.awaiter.domain.ApplicationStatus;
import core.contest5.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeamWaiter {

    @EmbeddedId
    private TeamWaiterId id;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @MapsId("teamId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    public void updateApplicationStatus(ApplicationStatus newStatus) {
        this.applicationStatus = newStatus;
    }

    public TeamWaiterDomain toDomain() {
        return new TeamWaiterDomain(
                id,
                post.toDomain(),
                team.toDomain(),
                registrationDate,
                applicationStatus //
        );
    }

    public static TeamWaiter from(TeamWaiterDomain domain) {
        return TeamWaiter.builder()
                .id(domain.getId())
                .post(Post.from(domain.getPost()))
                .team(Team.from(domain.getTeam()))
                .registrationDate(domain.getRegistrationDate())
                .applicationStatus(domain.getApplicationStatus())
                .build();
    }
}