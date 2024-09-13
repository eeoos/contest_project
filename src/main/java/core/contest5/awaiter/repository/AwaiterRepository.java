package core.contest5.awaiter.repository;

import core.contest5.awaiter.domain.Awaiter;
import core.contest5.member.domain.Member;
import core.contest5.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AwaiterRepository extends JpaRepository<Awaiter, Long> {
    List<Awaiter> findByPostId(Long postId);
    Optional<Awaiter> findByIdAndPostId(Long awaiterId, Long postId);

    boolean existsByPostAndMember(Post post, Member awaiter);


    void delete(Awaiter awaiter);
}