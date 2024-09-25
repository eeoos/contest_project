package core.contest5.team.service;

import core.contest5.team.domain.Team;
import core.contest5.team.domain.TeamDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository {
    TeamDomain save(TeamDomain team);
    TeamDomain findById(Long id);
    List<TeamDomain> findAll();
    void delete(TeamDomain team);
}