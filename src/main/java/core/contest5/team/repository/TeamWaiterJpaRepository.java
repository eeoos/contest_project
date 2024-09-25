package core.contest5.team.repository;

import core.contest5.team.domain.TeamWaiter;
import core.contest5.team.domain.TeamWaiterId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamWaiterJpaRepository extends JpaRepository<TeamWaiter, TeamWaiterId> {
    List<TeamWaiter> findByPostId(Long postId);
    Optional<TeamWaiter> findById(TeamWaiterId id);

    //    Optional<TeamWaiter> findByIdPostIdAndIdTeamId(Long postId, Long teamId);
    boolean existsById(TeamWaiterId id);
}
