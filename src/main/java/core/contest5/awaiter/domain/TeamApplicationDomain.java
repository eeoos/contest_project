package core.contest5.awaiter.domain;

import core.contest5.member.service.MemberDomain;
import core.contest5.post.service.PostDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class TeamApplicationDomain {

    //    private Long id;
//    private Long postId;
//    private Long applicantId;
//    private Long awaiterId;
    private TeamApplicationId id;
    private PostDomain postDomain;
    private MemberDomain applicantDomain;
    private MemberDomain awaiterDomain;
    private LocalDateTime applicationDate;
    private ApplicationStatus status;
}
