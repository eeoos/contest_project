package core.contest5.post.repository;

import core.contest5.member.domain.Member;
import core.contest5.member.service.MemberDomain;
import core.contest5.post.domain.Post;
import core.contest5.post.domain.PostField;
import core.contest5.post.domain.SortOption;
import core.contest5.post.service.PostDomain;
import core.contest5.post.service.PostInfo;
import core.contest5.post.service.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final PostJpaRepository postJpaRepository;
    @Override
    public Long save(PostInfo info, MemberDomain user) {
        Post post = Post.builder()
                .title(info.title())
                .content(info.content())
                .viewCount(0L)
                .bookmarkCount(0L)
                .writer(Member.from(user))
                .startDate(info.startDate())
                .endDate(info.endDate())
                .posterImage(info.posterImage())
                .qualification(info.qualification())
                .awardScale(info.awardScale())
                .host(info.host())
                .hostHomepageURL(info.hostHomepageURL())
                .postFields(info.postFields())
                .contestStatus(info.contestStatus())
                .build();

        return postJpaRepository.save(post).getId();

    }


    @Override
    public PostDomain findById(Long postId) {
        Post post = postJpaRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("Post not found")
        );

        return post.toDomain();
    }


    @Override
    public void update(PostDomain domain) {
        Post post = Post.builder()
                .id(domain.getId())
                .title(domain.getPostInfo().title())
                .content(domain.getPostInfo().content())
                .viewCount(domain.getViewCount())
                .bookmarkCount(domain.getBookmarkCount())
                .startDate(domain.getPostInfo().startDate())
                .endDate(domain.getPostInfo().endDate())
                .posterImage(domain.getPostInfo().posterImage())
                .qualification(domain.getPostInfo().qualification())
                .awardScale(domain.getPostInfo().awardScale())
                .host(domain.getPostInfo().host())
                .writer(Member.from(domain.getMember()))
                .hostHomepageURL(domain.getPostInfo().hostHomepageURL())
                .postFields(domain.getPostInfo().postFields())
                .contestStatus(domain.getPostInfo().contestStatus())
                .build();

        postJpaRepository.save(post);
    }
    @Override
    public void delete(Long postId) {
        Post post = postJpaRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("Post not found")
        );
        postJpaRepository.delete(post);
    }

    @Override
    public List<PostDomain> findAllSorted(SortOption sortOption) {
        List<Post> posts;
        switch (sortOption) {
            case LATEST:
                posts = postJpaRepository.findAllByOrderByCreatedAtDesc();
                break;
            case MOST_BOOKMARKS:
                posts = postJpaRepository.findAllByOrderByBookmarkCountDesc();
                break;
            case MOST_AWAITERS:
                posts = postJpaRepository.findAllOrderByAwaiterCountDesc();
                break;
            /*case MOST_REVIEWS:
                posts = postJpaRepository.findAllByOrderByReviewCountDesc();
                break;*/
            case CLOSEST_DEADLINE:
                posts = postJpaRepository.findAllByOrderByEndDateAsc();
                break;
            default:
                posts = postJpaRepository.findAll();
        }
        return posts.stream().map(Post::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<PostDomain> findByPostFieldsIn(Set<PostField> fields, SortOption sortOption) {
        List<Post> posts;
        switch (sortOption) {
            case LATEST:
                posts = postJpaRepository.findByPostFieldsInOrderByCreatedAtDesc(fields);
                break;
            case MOST_BOOKMARKS:
                posts = postJpaRepository.findByPostFieldsInOrderByBookmarkCountDesc(fields);
                break;
            case MOST_AWAITERS:
                posts = postJpaRepository.findByPostFieldsInOrderByAwaiterCountDesc(fields);
                break;
            /*case MOST_REVIEWS:
                posts = postJpaRepository.findByPostFieldsInOrderByReviewCountDesc(fields);
                break;*/
            case CLOSEST_DEADLINE:
                posts = postJpaRepository.findByPostFieldsInOrderByEndDateAsc(fields);
                break;
            default:
                posts = postJpaRepository.findByPostFieldsIn(fields);
        }
        return posts.stream().map(Post::toDomain).collect(Collectors.toList());
    }

    /*@Override
    public List<PostDomain> findAll() {
        List<Post> posts = postJpaRepository.findAll(); //

        return posts.stream()
                .map(Post::toDomain) //
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDomain> findByPostFieldsIn(Set<PostField> fields) {
        List<Post> posts = postJpaRepository.findByPostFieldsIn(fields);
        return posts.stream()
                .map(Post::toDomain)
                .collect(Collectors.toList());
    }*/

    @Override
    public void incrementViewCount(Long postId) {
        postJpaRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("Post not found")
        );

        postJpaRepository.incrementViewCount(postId);
    }

    @Override
    public void incrementBookmarkCount(Long postId) {
        postJpaRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("Post not fount")
        );
        postJpaRepository.incrementBookmarkCount(postId);

    }

    @Override
    public List<PostDomain> searchPosts(String keyword) {
        String likePattern = "%" + keyword + "%";
        return entityManager.createQuery(
                        "SELECT p FROM Post p WHERE " +
                                "LOWER(p.title) LIKE LOWER(:keyword) OR " +
                                "LOWER(p.content) LIKE LOWER(:keyword) OR " +
                                "LOWER(p.host) LIKE LOWER(:keyword) " +
                                "ORDER BY " +
                                "CASE " +
                                "  WHEN LOWER(p.title) = LOWER(:exactKeyword) THEN 1 " +
                                "  WHEN LOWER(p.title) LIKE LOWER(:startPattern) THEN 2 " +
                                "  ELSE 3 " +
                                "END, " +
                                "p.title ASC", Post.class)
                .setParameter("keyword", likePattern)
                .setParameter("exactKeyword", keyword.toLowerCase())
                .setParameter("startPattern", keyword.toLowerCase() + "%")
                .getResultList()
                .stream()
                .map(Post::toDomain)
                .collect(Collectors.toList());
    }
}
