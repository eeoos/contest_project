package core.contest5.awaiter.repository;

import core.contest5.awaiter.domain.TeamApplication;
import core.contest5.awaiter.domain.TeamApplicationId;
import core.contest5.member.domain.Member;
import core.contest5.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamApplicationJpaRepository extends JpaRepository<TeamApplication, TeamApplicationId> {
    Optional<TeamApplication> findById(TeamApplicationId id);

    boolean existsById(TeamApplicationId id);
}
