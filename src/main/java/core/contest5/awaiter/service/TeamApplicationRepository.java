package core.contest5.awaiter.service;

import core.contest5.awaiter.domain.TeamApplication;
import core.contest5.awaiter.domain.TeamApplicationDomain;
import core.contest5.awaiter.domain.TeamApplicationId;
import core.contest5.member.domain.Member;
import core.contest5.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface TeamApplicationRepository{
    TeamApplicationDomain findById(TeamApplicationId id);
    TeamApplicationDomain save(TeamApplicationDomain teamApplication);

    boolean existsById(TeamApplicationId id);
}