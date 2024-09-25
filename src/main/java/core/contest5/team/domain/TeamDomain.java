package core.contest5.team.domain;

import core.contest5.member.service.MemberDomain;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeamDomain {

    private Long id;
    private String name;
    private Long leaderId;
    @Builder.Default
    private Set<TeamMemberDomain> teamMembers = new HashSet<>();
    private String description;

    public TeamDomain(String name, Long leaderId, String description) {
        this.name = name;
        this.leaderId = leaderId;
        this.description = description;
//        this.addMember(leader);
    }

    /*public void addMember(MemberDomain member) {
        TeamMemberDomain teamMember = new TeamMemberDomain(this, member);
        this.teamMembers.add(teamMember);
    }*/

    public void addMember(TeamMemberDomain teamMember) {
        this.teamMembers.add(teamMember);
    }

    public void removeMember(MemberDomain member) {
        this.teamMembers.removeIf(teamMember -> teamMember.getMember().equals(member));
    }

}
