package core.contest5.team.domain;

import core.contest5.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class  Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private Member leader;

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamMember> teamMembers = new HashSet<>();

    private String description;

    @Builder
    public Team(String name, Member leader, String description) {
        this.name = name;
        this.leader = leader;
        this.description = description;
        this.teamMembers = new HashSet<>();
        this.addMember(leader); // 리더를 멤버로 추가
    }

    @Builder
    private Team(Long id, String name, Member leader, String description) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.description = description;
        this.teamMembers = new HashSet<>();
        this.addMember(leader);
    }

    public void addMember(Member member) {
        TeamMember teamMember = new TeamMember(this, member);
        this.teamMembers.add(teamMember);
    }

    public void removeMember(Member member) {
        this.teamMembers.removeIf(teamMember -> teamMember.getMember().equals(member));
    }

    public Set<TeamMember> getTeamMembers() {
        return Collections.unmodifiableSet(teamMembers);
    }

    public TeamDomain toDomain() {
        return new TeamDomain(
                id,
                name,
                leader.getId(),
                teamMembers.stream()
                        .map(tm -> new TeamMemberDomain(tm.getId(), new TeamDomain(this.id, this.name, this.leader.getId(), null, this.description), tm.getMember().toDomain(), tm.getJoinDate(), tm.getRole()))
                        .collect(Collectors.toSet()),
                description
        );
    }

    public static Team from(TeamDomain domain) {

        Team team = new Team(
                domain.getId(),
                domain.getName(),
                Member.from(domain.getLeaderId()),
                domain.getDescription()
        );

        if (domain.getTeamMembers() != null) {
            domain.getTeamMembers().forEach(tm ->
                    team.addMember(Member.from(tm.getMember()))
            );
        }

        return team;
    }
}