package core.contest5.awaiter.domain;

import core.contest5.member.domain.Member;
import core.contest5.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeamApplication {

    @EmbeddedId
    TeamApplicationId id;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @MapsId("applicantId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    private Member applicant;

    @MapsId("awaiterId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "awaiter_id")
    private Member awaiter;

    private LocalDateTime applicationDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    public static TeamApplication from(TeamApplicationId id) {
        return TeamApplication.builder()
                .id(id)
                .build();
    }

    public static TeamApplication from(TeamApplicationDomain domain) {
        return TeamApplication.builder()
                .id(new TeamApplicationId(domain.getId().getPostId(), domain.getId().getApplicantId(), domain.getId().getAwaiterId()))
                .post(Post.from(domain.getPostDomain()))
                .applicant(Member.from(domain.getApplicantDomain()))
                .awaiter(Member.from(domain.getAwaiterDomain()))
                .applicationDate(domain.getApplicationDate())
                .status(domain.getStatus())
                .build();
    }

    public TeamApplicationDomain toDomain() {
        return new TeamApplicationDomain(
                id,
                post.toDomain(),
                applicant.toDomain(),
                awaiter.toDomain(),
                applicationDate,
                status
        );
    }
}