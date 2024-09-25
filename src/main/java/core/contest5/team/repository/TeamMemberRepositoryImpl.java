package core.contest5.team.repository;

import core.contest5.global.exception.ResourceNotFoundException;
import core.contest5.member.domain.Member;
import core.contest5.member.repository.MemberJpaRepository;
import core.contest5.team.domain.Team;
import core.contest5.team.domain.TeamMember;
import core.contest5.team.domain.TeamMemberDomain;
import core.contest5.team.domain.TeamMemberId;
import core.contest5.team.service.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TeamMemberRepositoryImpl implements TeamMemberRepository {

    private final TeamMemberJpaRepository teamMemberJpaRepository;
    private final TeamJpaRepository teamJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    @Override
    public TeamMemberDomain save(TeamMemberDomain teamMemberDomain) {

        Team team = teamJpaRepository.findById(teamMemberDomain.getTeam().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        Member member = memberJpaRepository.findById(teamMemberDomain.getMember().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        Long nextMemberId = teamMemberJpaRepository.findMaxMemberIdByTeamId(team.getId()) + 1;

        TeamMember teamMember = TeamMember.builder()
                .id(new TeamMemberId(team.getId(), nextMemberId))
                .team(team)
                .member(member)
                .joinDate(teamMemberDomain.getJoinDate())
                .role(teamMemberDomain.getRole())
                .build();

        return teamMemberJpaRepository.save(teamMember).toDomain();
    }

    @Override
    public TeamMemberDomain findById(TeamMemberId id) {
        TeamMember teamMember = teamMemberJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("teamMember not found"));
        return teamMember.toDomain();
    }

    /*@Override
    public TeamMemberDomain findByTeamIdAndMemberId(Long teamId, Long memberId) {
        TeamMember teamMember = teamMemberJpaRepository.findByIdTeamIdAndIdMemberId(teamId, memberId);
        return teamMember != null ? teamMember.toDomain() : null;
    }*/

    @Override
    public List<TeamMemberDomain> findByTeamId(Long teamId) {
        return teamMemberJpaRepository.findByTeamId(teamId)
                .stream()
                .map(TeamMember::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(TeamMemberDomain teamMemberDomain) {
        teamMemberJpaRepository.delete(TeamMember.from(teamMemberDomain));

    }
}
