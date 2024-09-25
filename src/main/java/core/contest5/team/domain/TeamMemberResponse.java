package core.contest5.team.domain;

import core.contest5.awaiter.domain.ApplicationStatus;
import core.contest5.member.domain.memberinfo.Certificate;
import core.contest5.member.domain.memberinfo.MemberDuty;
import core.contest5.member.domain.memberinfo.MemberField;
import core.contest5.member.domain.memberinfo.TechStack;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberResponse {
    private Long teamId;
    private Long memberId;

    private String memberName;
    private String profileImage;
    private String field;
    private String duty;

    private List<String> techStacks;
    private List<String> certificates;

    public static TeamMemberResponse from(TeamMemberDomain domain) {
        return new TeamMemberResponse(
                domain.getId().getTeamId(),
                domain.getId().getMemberId(),
                domain.getMember().getMemberInfo().name(),
                domain.getMember().getMemberInfo().profileImage(),

                Optional.ofNullable(domain.getMember().getMemberInfo().memberField())
                        .map(MemberField::getFieldName).orElse(null),
                Optional.ofNullable(domain.getMember().getMemberInfo().memberDuty())
                        .map(MemberDuty::getDutyName).orElse(null),
                Optional.ofNullable(domain.getMember().getMemberInfo().techStacks())
                        .map(techs -> techs.stream().map(TechStack::getName).collect(Collectors.toList()))
                        .orElse(Collections.emptyList()),
                Optional.ofNullable(domain.getMember().getMemberInfo().certificates())
                        .map(certs -> certs.stream().map(Certificate::getName).collect(Collectors.toList()))
                        .orElse(Collections.emptyList())
        );
    }
}
