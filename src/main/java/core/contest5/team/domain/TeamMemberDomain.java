package core.contest5.team.domain;

import core.contest5.member.service.MemberDomain;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeamMemberDomain {
    private TeamMemberId id;
    private TeamDomain team;
    private MemberDomain member;
    private LocalDateTime joinDate;
    private TeamMemberRole role;

    public TeamMemberDomain(TeamDomain team, MemberDomain member) {
        this.team = team;
        this.member = member;
        this.role = TeamMemberRole.MEMBER;
        this.joinDate = LocalDateTime.now();
        this.id = new TeamMemberId(team.getId(), member.getId());
    }


}
