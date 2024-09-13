package core.contest5.team.service;

import core.contest5.global.exception.ResourceNotFoundException;
import core.contest5.member.domain.Member;
import core.contest5.member.domain.MemberResponse;
import core.contest5.member.repository.MemberJpaRepository;
import core.contest5.member.service.MemberRepository;
import core.contest5.team.domain.Team;
import core.contest5.team.domain.TeamCreateRequestDto;
import core.contest5.team.domain.TeamResponseDto;
import core.contest5.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {
    private final TeamRepository teamRepository;
    private final MemberJpaRepository memberRepository;

    @Transactional
    public TeamResponseDto createTeam(TeamCreateRequestDto requestDto) {
        Optional<Member> optionalLeader = memberRepository.findById(requestDto.getLeaderId());
        Member leader = optionalLeader.get();

        log.info("leader={}", leader);

        Team team = Team.builder()
                .name(requestDto.getName())
                .leader(leader)
                .description(requestDto.getDescription())
                .build();

        Team savedTeam = teamRepository.save(team);
        return convertToDto(savedTeam);
    }

    public List<MemberResponse> getTeamMembers(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        return team.getMembers().stream()
                .map(this::convertToMemberDto)
                .collect(Collectors.toList());
    }

    public MemberResponse getTeamMemberProfile(Long teamId, Long memberId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        Member member = team.getMembers().stream()
                .filter(m -> m.getId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Member not found in the team"));

        return convertToMemberDto(member);
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
    }
}