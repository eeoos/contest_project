package core.contest5.team.service;

import core.contest5.team.domain.TeamMember;
import core.contest5.team.domain.TeamMemberDomain;
import core.contest5.team.domain.TeamMemberId;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository {
    TeamMemberDomain save(TeamMemberDomain teamMemberDomain); //
    TeamMemberDomain findById(TeamMemberId id);
//    TeamMemberDomain findByTeamIdAndMemberId(Long teamId, Long memberId);
    List<TeamMemberDomain> findByTeamId(Long teamId);
    void delete(TeamMemberDomain teamMemberDomain);
}
