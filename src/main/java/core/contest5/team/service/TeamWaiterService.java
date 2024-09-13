package core.contest5.team.service;

import core.contest5.awaiter.domain.ApplicationStatus;
import core.contest5.global.exception.ResourceNotFoundException;
import core.contest5.post.domain.Post;
import core.contest5.post.service.PostDomain;
import core.contest5.post.service.PostRepository;
import core.contest5.team.domain.Team;
import core.contest5.team.domain.TeamWaiter;
import core.contest5.team.domain.TeamWaiterResponseDto;
import core.contest5.team.repository.TeamRepository;
import core.contest5.team.repository.TeamWaiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamWaiterService {
    private final TeamWaiterRepository teamWaiterRepository;
    private final PostRepository postRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public TeamWaiterResponseDto registerTeamWaiter(Long postId, Long teamId) {
        PostDomain postDomain = postRepository.findById(postId);


        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        // Check if the team is already waiting for this post
        if (teamWaiterRepository.findByPostAndTeam(Post.from(postDomain), team).isPresent()) {
            throw new IllegalStateException("이미 등록하셨습니다.");
        }

        TeamWaiter teamWaiter = TeamWaiter.builder()
                .post(Post.from(postDomain))
                .team(team)
                .registrationDate(LocalDateTime.now())
                .applicationStatus(ApplicationStatus.PENDING)
                .build();

        TeamWaiter savedTeamWaiter = teamWaiterRepository.save(teamWaiter);
        return convertToDto(savedTeamWaiter);
    }

    public List<TeamWaiterResponseDto> getTeamWaiterList(Long postId) {
        PostDomain postDomain = postRepository.findById(postId);


        return teamWaiterRepository.findByPost(Post.from(postDomain)).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TeamWaiterResponseDto convertToDto(TeamWaiter teamWaiter) {
        return TeamWaiterResponseDto.builder()
                .id(teamWaiter.getId())
                .postId(teamWaiter.getPost().getId())
                .teamId(teamWaiter.getTeam().getId())
                .teamName(teamWaiter.getTeam().getName())
                .teamLeaderName(teamWaiter.getTeam().getLeader().getName())
                .registrationDate(teamWaiter.getRegistrationDate())
                .applicationStatus(teamWaiter.getApplicationStatus())
                .build();
    }

    @Transactional
    public void deleteTeamWaiter(Long postId, Long teamWaiterId) {

        TeamWaiter teamWaiter = teamWaiterRepository.findById(teamWaiterId)
                .orElseThrow(() -> new ResourceNotFoundException("TeamWaiter not found"));

        if (!teamWaiter.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("TeamWaiter does not belong to the specified post");
        }

        teamWaiterRepository.delete(teamWaiter);
    }
}