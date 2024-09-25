package core.contest5.awaiter.repository;

import core.contest5.awaiter.domain.TeamApplication;
import core.contest5.awaiter.domain.TeamApplicationDomain;
import core.contest5.awaiter.domain.TeamApplicationId;
import core.contest5.awaiter.service.TeamApplicationRepository;
import core.contest5.global.exception.ResourceNotFoundException;
import core.contest5.member.domain.Member;
import core.contest5.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamApplicationRepositoryImpl implements TeamApplicationRepository {

    private final TeamApplicationJpaRepository teamApplicationJpaRepository;

    @Override
    public TeamApplicationDomain findById(TeamApplicationId id) {
        return teamApplicationJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TeamApplication not found"))
                .toDomain();
    }

    @Override
    public TeamApplicationDomain save(TeamApplicationDomain teamApplicationDomain) {


        TeamApplication savedEntity = teamApplicationJpaRepository.save(TeamApplication.from(teamApplicationDomain));
        return savedEntity.toDomain();
    }

    @Override
    public boolean existsById(TeamApplicationId id) {
        return teamApplicationJpaRepository.existsById(id);
    }
}