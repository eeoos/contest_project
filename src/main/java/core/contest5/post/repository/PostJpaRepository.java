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

    @Query("SELECT p FROM Post p JOIN p.postFields f WHERE f IN :fields")
    List<Post> findByPostFieldsIn(@Param("fields") Set<PostField> fields);

    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    void incrementViewCount(Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.bookmarkCount = p.bookmarkCount + 1 WHERE p.id = :postId")
    void incrementBookmarkCount(Long postId);

}

