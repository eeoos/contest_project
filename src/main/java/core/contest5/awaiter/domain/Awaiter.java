package core.contest5.awaiter.domain;

import core.contest5.member.domain.Member;
import core.contest5.post.domain.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Awaiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;/**/

    // 프로필 정보
    private String profileDescription;

    // 팀원 신청 상태
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    public void updateApplicationStatus(ApplicationStatus newStatus) {
        this.applicationStatus = newStatus;
    }

    public static Awaiter from(AwaiterDomain awaiter) {
        return Awaiter.builder()
                .id(awaiter.getId())
                .post(Post.from(awaiter.getPostId()))
                .member(Member.from(awaiter.getMemberId()))
                .registrationDate(awaiter.getRegistrationDate())
                .profileDescription(awaiter.getProfileDescription())
                .applicationStatus(awaiter.getApplicationStatus())
                .build();
    }

    public AwaiterDomain toDomain() {
        return new AwaiterDomain(
                id,
                post.getId(),
                member.getId(),
                registrationDate,
                profileDescription,
                applicationStatus
        );
    }
}