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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    private Member applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "awaiter_id")
    private Member awaiter;

    private LocalDateTime applicationDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
}