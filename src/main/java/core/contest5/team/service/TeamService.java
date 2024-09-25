package core.contest5.team.service;

import core.contest5.global.exception.ResourceNotFoundException;
import core.contest5.member.domain.Member;
import core.contest5.member.domain.MemberResponse;
import core.contest5.member.repository.MemberJpaRepository;
import core.contest5.member.service.MemberDomain;
import core.contest5.member.service.MemberReader;
import core.contest5.member.service.MemberRepository;
import core.contest5.team.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {
    private final TeamRepository teamRepository;
    private final MemberReader memberReader;
    private final TeamMemberRepository teamMemberRepository;
    @Transactional
    public TeamDomain createTeam(TeamCreateRequestDto requestDto) {

        MemberDomain leader = memberReader.read(requestDto.getLeaderId());
        log.info("leader={}", leader);

        TeamDomain teamDomain = TeamDomain.builder()
                .name(requestDto.getName())
                .leaderId(leader.getId())
                .description(requestDto.getDescription())
                .build();

        //팀을 먼저 저장한 후,
        TeamDomain savedTeamDomain = teamRepository.save(teamDomain);

        //leader를 teamMember로 생성 후 저장
        TeamMemberDomain leaderMember = TeamMemberDomain.builder()
                .team(savedTeamDomain)
                .member(leader)
                .joinDate(LocalDateTime.now())
                .role(TeamMemberRole.LEADER)
                .build();

        TeamMemberDomain savedLeaderMember = teamMemberRepository.save(leaderMember);

        //저장된 teamMember를 team에 추가
        savedTeamDomain.addMember(savedLeaderMember);

        return savedTeamDomain;
    }

    public TeamDomain getTeam(Long teamId) {
        return teamRepository.findById(teamId);
    }
    public TeamMemberDomain getTeamMemberProfile(Long teamId, Long memberId) {
        TeamMemberId id = new TeamMemberId(teamId, memberId);
        return teamMemberRepository.findById(id);
    }


    /*public List<MemberResponse> getTeamMembers(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        return team.getMembers().stream()
                .map(this::convertToMemberDto)
                .collect(Collectors.toList());
    }

    private TeamResponseDto convertToDto(Team team) {
        return TeamResponseDto.builder()
                .id(team.getId())
                .name(team.getName())
                .leaderId(team.getLeader().getId())
                .leaderName(team.getLeader().getName())
                .description(team.getDescription())
                .build();
    }

    private MemberResponse convertToMemberDto(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .profileImage(member.getProfileImage())
                .role(member.getMemberRole())
                .build();
    }*/
}