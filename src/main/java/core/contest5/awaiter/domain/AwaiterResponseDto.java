package core.contest5.awaiter.domain;

import core.contest5.member.domain.Member;
import core.contest5.member.domain.memberinfo.Certificate;
import core.contest5.member.domain.memberinfo.TechStack;
import core.contest5.member.service.MemberDomain;
import core.contest5.team.domain.TeamMemberDomain;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AwaiterResponseDto {
    private Long postId;
    private Long memberId;

    private String memberName;
    private String profileImage;
    private String field;
    private String duty;
    private List<String> techStacks;
    private List<String> certificates;
    private ApplicationStatus applicationStatus;

    public static AwaiterResponseDto from(AwaiterDomain domain) {
        return new AwaiterResponseDto(
                domain.getId().getPostId(),
                domain.getId().getMemberId(),
                domain.getMember().getMemberInfo().name(),
                domain.getMember().getMemberInfo().profileImage(),
                domain.getMember().getMemberInfo().memberField().getFieldName(),
                domain.getMember().getMemberInfo().memberDuty().getDutyName(),
                domain.getMember().getMemberInfo().techStacks().stream().map(TechStack::getName).toList(),
                domain.getMember().getMemberInfo().certificates().stream().map(Certificate::getName).toList(),
                domain.getApplicationStatus()
        );
    }
}