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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    public void updateApplicationStatus(ApplicationStatus newStatus) {
        this.applicationStatus = newStatus;
    }
}