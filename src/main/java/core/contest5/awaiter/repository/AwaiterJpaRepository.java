package core.contest5.awaiter.repository;

import core.contest5.awaiter.domain.Awaiter;
import core.contest5.awaiter.domain.AwaiterId;
import core.contest5.member.domain.Member;
import core.contest5.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AwaiterJpaRepository extends JpaRepository<Awaiter, AwaiterId> {
    List<Awaiter> findByPostId(Long postId);

    Optional<Awaiter> findById(AwaiterId id);
    boolean existsById(AwaiterId id);
}
