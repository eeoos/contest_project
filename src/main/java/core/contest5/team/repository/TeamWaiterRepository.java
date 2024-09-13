package core.contest5.team.repository;

import core.contest5.post.domain.Post;
import core.contest5.team.domain.Team;
import core.contest5.team.domain.TeamWaiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamWaiterRepository extends JpaRepository<TeamWaiter, Long> {
    List<TeamWaiter> findByPost(Post post);
    Optional<TeamWaiter> findByPostAndTeam(Post post, Team team);
}