package core.contest5.team.service;

import core.contest5.post.domain.Post;
import core.contest5.post.service.PostDomain;
import core.contest5.team.domain.*;

import java.util.List;
import java.util.Optional;

public interface TeamWaiterRepository  {
    TeamWaiterDomain save(TeamWaiterDomain teamWaiterDomain);
    TeamWaiterDomain findById(TeamWaiterId id);
    List<TeamWaiterDomain> findByPostId(Long postId);
//    Optional<TeamWaiterDomain> findByPostAndTeam(PostDomain postDomain, TeamDomain teamDomain);
    void delete(TeamWaiterDomain teamWaiterDomain);

    boolean existsById(TeamWaiterId id);
}