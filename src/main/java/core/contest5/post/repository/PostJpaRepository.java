package core.contest5.post.repository;

import core.contest5.post.domain.Post;
import core.contest5.post.domain.PostField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    @Query("SELECT DISTINCT p FROM Post p JOIN p.postFields f WHERE f IN :fields")
    List<Post> findByPostFieldsIn(@Param("fields") Set<PostField> fields);

    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    void incrementViewCount(Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.bookmarkCount = p.bookmarkCount + 1 WHERE p.id = :postId")
    void incrementBookmarkCount(Long postId);

    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByOrderByBookmarkCountDesc();
//    List<Post> findAllByOrderByReviewCountDesc();
    List<Post> findAllByOrderByEndDateAsc();


    @Query("SELECT p FROM Post p LEFT JOIN Awaiter a ON p.id = a.post.id GROUP BY p ORDER BY COUNT(a) DESC")
    List<Post> findAllOrderByAwaiterCountDesc();

    List<Post> findByPostFieldsInOrderByCreatedAtDesc(Set<PostField> fields);
//    List<Post> findByPostFieldsInOrderByReviewCountDesc(Set<PostField> fields);



    @Query("SELECT p FROM Post p JOIN p.postFields f WHERE f IN :fields GROUP BY p ORDER BY p.bookmarkCount DESC")
    List<Post> findByPostFieldsInOrderByBookmarkCountDesc(@Param("fields") Set<PostField> fields);

    @Query("SELECT p FROM Post p JOIN p.postFields f WHERE f IN :fields GROUP BY p ORDER BY p.endDate ASC")
    List<Post> findByPostFieldsInOrderByEndDateAsc(@Param("fields") Set<PostField> fields);

    @Query("SELECT p FROM Post p LEFT JOIN Awaiter a ON p.id = a.post.id JOIN p.postFields f WHERE f IN :fields GROUP BY p ORDER BY COUNT(a) DESC")
    List<Post> findByPostFieldsInOrderByAwaiterCountDesc(@Param("fields") Set<PostField> fields);

}

