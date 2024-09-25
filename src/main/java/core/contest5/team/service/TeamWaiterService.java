package core.contest5.team.service;

import core.contest5.awaiter.domain.ApplicationStatus;
import core.contest5.post.service.PostDomain;
import core.contest5.post.service.PostRepository;
import core.contest5.team.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamWaiterService {
    private final TeamWaiterRepository teamWaiterRepository;
    private final PostRepository postRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public TeamWaiterDomain registerTeamWaiter(Long postId, Long teamId) {

        PostDomain postDomain = postRepository.findById(postId);
        TeamDomain teamDomain = teamRepository.findById(teamId);

        TeamWaiterId id = new TeamWaiterId(postDomain.getId(), teamDomain.getId());

        if (teamWaiterRepository.existsById(id)) {
            throw new IllegalStateException("이미 등록하셨습니다.");
        }

        TeamWaiterDomain teamWaiterDomain = TeamWaiterDomain.builder()
                .id(id)
                .post(postDomain)
                .team(teamDomain)
                .registrationDate(LocalDateTime.now())
                .applicationStatus(ApplicationStatus.PENDING)
                .build();

        return teamWaiterRepository.save(teamWaiterDomain);
    }

    public List<TeamWaiterDomain> getTeamWaiterList(Long postId) {
        return teamWaiterRepository.findByPostId(postId);
    }

    @Transactional
    public void deleteTeamWaiter(Long postId, Long teamId) {
        TeamWaiterId id = new TeamWaiterId(postId, teamId);
        TeamWaiterDomain teamWaiterDomain = teamWaiterRepository.findById(id);
        teamWaiterRepository.delete(teamWaiterDomain);
    }

    @Transactional
    public TeamWaiterDomain getTeamProfile(Long postId, Long teamId) {
        TeamWaiterId id = new TeamWaiterId(postId, teamId);
        return teamWaiterRepository.findById(id);
    }
}