package core.contest5.team.repository;

import core.contest5.global.exception.ResourceNotFoundException;
import core.contest5.member.domain.Member;
import core.contest5.member.repository.MemberJpaRepository;
import core.contest5.team.domain.Team;
import core.contest5.team.domain.TeamDomain;
import core.contest5.team.domain.TeamMember;
import core.contest5.team.service.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepository {
    private final TeamJpaRepository teamJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final TeamMemberJpaRepository teamMemberJpaRepository;
    @Override
    public TeamDomain save(TeamDomain teamDomain) {

        Member leader = memberJpaRepository.findById(teamDomain.getLeaderId())
                .orElseThrow(() -> new ResourceNotFoundException("leader not found"));

        List<TeamMember> teamMembers = teamMemberJpaRepository.findByTeamId(teamDomain.getId());
        Team team = Team.builder()
                .name(teamDomain.getName())
                .leader(leader)
                .description(teamDomain.getDescription())
                .teamMembers(new HashSet<>(teamMembers))
                .build();


        return teamJpaRepository.save(team).toDomain();
    }

    @Override
    public TeamDomain findById(Long id) {
        Team team = teamJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not fount"));
        return team.toDomain();
    }

    @Override
    public List<TeamDomain> findAll() {
        return teamJpaRepository.findAll().stream().map(Team::toDomain).toList();
    }

    @Override
    public void delete(TeamDomain teamDomain) {
        teamJpaRepository.delete(Team.from(teamDomain));
    }
}
