package core.contest5.team.repository;

import core.contest5.global.exception.ResourceNotFoundException;
import core.contest5.post.service.PostDomain;
import core.contest5.team.domain.*;
import core.contest5.team.service.TeamWaiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamWaiterRepositoryImpl implements TeamWaiterRepository {

    private final TeamWaiterJpaRepository teamWaiterJpaRepository;

    @Override
    public TeamWaiterDomain save(TeamWaiterDomain teamWaiterDomain) {
        /*TeamWaiter teamWaiter = TeamWaiter.builder()
                .id(teamWaiterDomain.getId())
                .post(Post.from(teamWaiterDomain.getPost()))
                .team(Team.from(teamWaiterDomain.getTeam()))
                .registrationDate(teamWaiterDomain.getRegistrationDate())
                .applicationStatus(teamWaiterDomain.getApplicationStatus())
                .build();*/
        TeamWaiter teamWaiter = TeamWaiter.from(teamWaiterDomain);
        return teamWaiterJpaRepository.save(teamWaiter).toDomain();
    }

    @Override
    public TeamWaiterDomain findById(TeamWaiterId id) {
        return teamWaiterJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("teamWaiter not found"))
                .toDomain();
    }

    @Override
    public List<TeamWaiterDomain> findByPostId(Long postId) {
        return teamWaiterJpaRepository.findByPostId(postId)
                .stream()
                .map(TeamWaiter::toDomain)
                .toList();
    }

    @Override
    public void delete(TeamWaiterDomain teamWaiterDomain) {
        teamWaiterJpaRepository.delete(TeamWaiter.from(teamWaiterDomain));
    }

    @Override
    public boolean existsById(TeamWaiterId id) {
        return teamWaiterJpaRepository.existsById(id);
    }
}
