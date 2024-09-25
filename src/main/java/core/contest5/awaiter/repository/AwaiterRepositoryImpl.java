package core.contest5.awaiter.repository;

import core.contest5.awaiter.domain.Awaiter;
import core.contest5.awaiter.domain.AwaiterDomain;
import core.contest5.awaiter.domain.AwaiterId;
import core.contest5.awaiter.service.AwaiterRepository;
import core.contest5.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AwaiterRepositoryImpl implements AwaiterRepository {

    private final AwaiterJpaRepository awaiterJpaRepository;

    @Override
    public List<AwaiterDomain> findByPostId(Long postId) {
        List<Awaiter> awaiters = awaiterJpaRepository.findByPostId(postId);
        return awaiters.stream().map(Awaiter::toDomain).toList();
    }

    @Override
    public AwaiterDomain findById(AwaiterId id) {
        Awaiter awaiter = awaiterJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Awaiter not found"));
        return awaiter.toDomain();

    }

    @Override
    public boolean existsById(AwaiterId id) {
        return awaiterJpaRepository.existsById(id);
    }


    @Override
    public void delete(AwaiterDomain awaiter) {
        awaiterJpaRepository.deleteById(awaiter.getId());
    }

    @Override
    public void save(AwaiterDomain awaiter) {
        Awaiter awaiterEntity = Awaiter.from(awaiter);
        awaiterJpaRepository.save(awaiterEntity);
    }


    /*@Override //대기는 1개 이상의 공모전에 걸 수 있기 때문에 findByIdAndPostId 사용
    public AwaiterDomain findById(Long awaiterId) {
        Awaiter awaiter = awaiterJpaRepository.findById(awaiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Awaiter not found"));
        return awaiter.toDomain();
    }*/
}
