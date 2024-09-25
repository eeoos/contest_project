package core.contest5.awaiter.domain;

import core.contest5.member.domain.Member;
import core.contest5.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Awaiter {

    @EmbeddedId
    private AwaiterId id;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    // 팀원 신청 상태
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    public Awaiter(Post post, Member member) {
        this.id = new AwaiterId(member.getId(), post.getId());
        this.post = post;
        this.member = member;
    }

    public void updateApplicationStatus(ApplicationStatus newStatus) {
        this.applicationStatus = newStatus;
    }

    public static Awaiter from(AwaiterDomain domain) {
        return Awaiter.builder()
                .id(new AwaiterId(domain.getId().getMemberId(), domain.getId().getPostId()))
                .post(Post.from(domain.getId().getPostId()))
                .member(Member.from(domain.getId().getMemberId()))
                .applicationStatus(domain.getApplicationStatus())
                .build();
    }


    public static Awaiter from(AwaiterId id) {
        return Awaiter.builder()
                .id(id)
                .build();
    }
    public AwaiterDomain toDomain() {
        return new AwaiterDomain(
                id,
                post.toDomain(),
                member.toDomain(),
                applicationStatus
        );
    }

    // 관계 변경을 위한 메서드
    public void changePost(Post newPost) {
        if (this.post != null) {
            this.post.getAwaiters().remove(this);
        }
        this.post = newPost;
        if (newPost != null) {
            newPost.getAwaiters().add(this);
        }
    }

}