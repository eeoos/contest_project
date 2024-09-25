package core.contest5.team.repository;

import core.contest5.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamJpaRepository extends JpaRepository<Team, Long> {
}
