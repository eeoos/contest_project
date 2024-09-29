package core.contest5.post.repository;

import core.contest5.post.domain.ContestStatus;
import core.contest5.post.domain.Post;
import core.contest5.post.domain.PostField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    List<Post> findByContestStatusIn(List<ContestStatus> statuses);

    @Query("SELECT p FROM Post p WHERE p.id = :id AND p.contestStatus <> :status")
    Optional<Post> findByIdAndContestStatusNot(@Param("id") Long id, @Param("status") ContestStatus status);
    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    void incrementViewCount(Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.bookmarkCount = p.bookmarkCount + 1 WHERE p.id = :postId")
    void incrementBookmarkCount(Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.bookmarkCount = p.bookmarkCount - 1 WHERE p.id = :postId AND p.bookmarkCount > 0") //0일 때 방지 작업: O
    void decrementBookmarkCount(Long postId);

    /*@Modifying
    @Query("UPDATE Post p SET p.awaiterCount = p.awaiterCount + 1 WHERE p.id = :postId")
    void incrementAwaiterCount(Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.awaiterCount = p.awaiterCount - 1 WHERE p.id = :postId AND p.awaiterCount > 0") //0일 때 방지 작업: O
    void decrementAwaiterCount(Long postId);*/


    @Query("SELECT p FROM Post p WHERE p.contestStatus <> :status ORDER BY p.createdAt DESC")
    List<Post> findAllExceptStatusOrderByCreatedAtDesc(@Param("status") ContestStatus status);

    @Query("SELECT p FROM Post p WHERE p.contestStatus <> :status ORDER BY p.bookmarkCount DESC")
    List<Post> findAllExceptStatusOrderByBookmarkCountDesc(@Param("status") ContestStatus status);

    @Query("SELECT p FROM Post p WHERE p.contestStatus <> :status ORDER BY SIZE(p.awaiters) DESC")
    List<Post> findAllExceptStatusOrderByAwaiterCountDesc(@Param("status") ContestStatus status);

    @Query("SELECT p FROM Post p WHERE p.contestStatus <> :status ORDER BY p.endDate ASC")
    List<Post> findAllExceptStatusOrderByEndDateAsc(@Param("status") ContestStatus status);

    @Query("SELECT p FROM Post p JOIN p.postFields f WHERE f IN :fields AND p.contestStatus <> :status ORDER BY p.createdAt DESC")
    List<Post> findByPostFieldsInAndContestStatusNotOrderByCreatedAtDesc(@Param("fields") Set<PostField> fields, @Param("status") ContestStatus status);

    @Query("SELECT p FROM Post p JOIN p.postFields f WHERE f IN :fields AND p.contestStatus <> :status ORDER BY p.bookmarkCount DESC")
    List<Post> findByPostFieldsInAndContestStatusNotOrderByBookmarkCountDesc(@Param("fields") Set<PostField> fields, @Param("status") ContestStatus status);

    @Query("SELECT p FROM Post p JOIN p.postFields f WHERE f IN :fields AND p.contestStatus <> :status ORDER BY SIZE(p.awaiters) DESC")
    List<Post> findByPostFieldsInAndContestStatusNotOrderByAwaiterCountDesc(@Param("fields") Set<PostField> fields, @Param("status") ContestStatus status);

    @Query("SELECT p FROM Post p JOIN p.postFields f WHERE f IN :fields AND p.contestStatus <> :status ORDER BY p.endDate ASC")
    List<Post> findByPostFieldsInAndContestStatusNotOrderByEndDateAsc(@Param("fields") Set<PostField> fields, @Param("status") ContestStatus status);

    @Query("SELECT p FROM Post p WHERE " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.host) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "p.contestStatus <> :status " +
            "ORDER BY " +
            "CASE " +
            "  WHEN LOWER(p.title) = LOWER(:exactKeyword) THEN 1 " +
            "  WHEN LOWER(p.title) LIKE LOWER(CONCAT(:exactKeyword, '%')) THEN 2 " +
            "  ELSE 3 " +
            "END, " +
            "p.title ASC")
    List<Post> searchPostsExceptStatus(@Param("keyword") String keyword,
                                       @Param("exactKeyword") String exactKeyword,
                                       @Param("status") ContestStatus status);
}

