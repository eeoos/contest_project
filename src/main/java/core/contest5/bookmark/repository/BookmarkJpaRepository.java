package core.contest5.bookmark.repository;

import core.contest5.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkJpaRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByPost_IdAndMember_Id(Long postId, Long memberId);

    void deleteByPost_IdAndMember_Id(Long postId, Long memberId);

    long countByPost_Id(Long postId);
}
