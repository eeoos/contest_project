package core.contest5.awaiter.service;

import core.contest5.awaiter.domain.Awaiter;
import core.contest5.awaiter.domain.AwaiterDomain;
import core.contest5.awaiter.domain.AwaiterId;
import core.contest5.member.domain.Member;
import core.contest5.member.service.MemberDomain;
import core.contest5.post.domain.Post;
import core.contest5.post.service.PostDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface AwaiterRepository{
    List<AwaiterDomain> findByPostId(Long postId);
    AwaiterDomain findById(AwaiterId awaiterId);
    boolean existsById(AwaiterId awaiterId);

    void delete(AwaiterDomain awaiter);
    void save(AwaiterDomain awaiter);
//    AwaiterDomain findById(Long awaiterId)

}