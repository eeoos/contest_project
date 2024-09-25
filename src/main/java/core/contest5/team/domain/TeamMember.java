package core.contest5.team.domain;

import core.contest5.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "team_members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeamMember {

    @EmbeddedId
    private TeamMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("teamId")
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime joinDate;

    @Enumerated(EnumType.STRING)
    private TeamMemberRole role;

    public TeamMember(Team team, Member member) {
        this.id = new TeamMemberId(team.getId(), member.getId());
        this.team = team;
        this.member = member;
        this.role = TeamMemberRole.MEMBER;
        this.joinDate = LocalDateTime.now();
    }

    public TeamMemberDomain toDomain() {
        return new TeamMemberDomain(
                id,
//                new TeamDomain(team.getId(), team.getName(), team.getLeader().getId(), null, team.getDescription()),
                team.toDomain(),
                member.toDomain(),
                joinDate,
                role // TeamMemberRole.MEMBER
        );
    }

    public static TeamMember from(TeamMemberDomain domain) {
        return TeamMember.builder()
                .id(domain.getId())
                .team(Team.from(domain.getTeam()))
                .member(Member.from(domain.getMember()))
                .joinDate(domain.getJoinDate())
                .role(domain.getRole())
                .build();
    }
}
