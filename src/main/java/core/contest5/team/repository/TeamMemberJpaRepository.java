package core.contest5.team.repository;

import core.contest5.team.domain.TeamMember;
import core.contest5.team.domain.TeamMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamMemberJpaRepository extends JpaRepository<TeamMember, TeamMemberId> {
    @Query("SELECT COALESCE(MAX(tm.id.memberId), 0) FROM TeamMember tm WHERE tm.id.teamId = :teamId")
    Long findMaxMemberIdByTeamId(@Param("teamId") Long teamId);

//    List<TeamMember> findByIdTeamId(Long teamId);
    List<TeamMember> findByTeamId(Long teamId);
}
