package core.contest5.post.repository;

import core.contest5.awaiter.domain.Awaiter;
import core.contest5.global.exception.ResourceNotFoundException;
import core.contest5.member.domain.Member;
import core.contest5.member.service.MemberDomain;
import core.contest5.post.domain.ContestStatus;
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
                .attachedFiles(info.attachedFiles())
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
        return postJpaRepository.findByIdAndContestStatusNot(postId, ContestStatus.CLOSED)
                .map(Post::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Active post not found with id: " + postId));
    }


    @Override
    public void update(PostDomain domain) {

        Post existingPost = postJpaRepository.findById(domain.getId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Post updatedPost = Post.from(domain);
        if (existingPost.getAwaiters() != null) {
            updatedPost.updateAwaiters(existingPost.getAwaiters());
        }

        postJpaRepository.save(updatedPost);
    }

    @Override
    public void delete(Long postId) {
        Post post = postJpaRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found")
        );
        postJpaRepository.delete(post);
    }

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
                () -> new ResourceNotFoundException("Post not found")
        );
        postJpaRepository.incrementBookmarkCount(postId);

    }

    @Override
    public void decrementBookmarkCount(Long postId) {
        postJpaRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found")
        );
        postJpaRepository.decrementBookmarkCount(postId);
    }

    @Override
    public void incrementAwaiterCount(Long postId) {
        postJpaRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        postJpaRepository.incrementAwaiterCount(postId);
//        postJpaRepository.save(post);
    }

    @Override
    public void decrementAwaiterCount(Long postId) {
        postJpaRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        postJpaRepository.decrementAwaiterCount(postId);
//        postJpaRepository.save(post);
    }

    @Override
    public List<PostDomain> searchPosts(String keyword) {
        return postJpaRepository.searchPostsExceptStatus(keyword, keyword, ContestStatus.CLOSED)
                .stream()
                .map(Post::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public PostDomain save(PostDomain postDomain) {
        Post post = Post.from(postDomain);
        Post savedPost = postJpaRepository.save(post);
        return savedPost.toDomain();
    }

    @Override
    public List<PostDomain> findAllExceptStatusSorted(ContestStatus status, SortOption sortOption) {
        List<Post> posts;
        switch (sortOption) {
            case LATEST:
                posts = postJpaRepository.findAllExceptStatusOrderByCreatedAtDesc(status);
                break;
            case MOST_BOOKMARKS:
                posts = postJpaRepository.findAllExceptStatusOrderByBookmarkCountDesc(status);
                break;
            case MOST_AWAITERS:
                posts = postJpaRepository.findAllExceptStatusOrderByAwaiterCountDesc(status);
                break;
            case CLOSEST_DEADLINE:
                posts = postJpaRepository.findAllExceptStatusOrderByEndDateAsc(status);
                break;
            default:
                posts = postJpaRepository.findAllExceptStatusOrderByCreatedAtDesc(status);
        }
        return posts.stream().map(Post::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<PostDomain> findByPostFieldsInAndContestStatusNotSorted(Set<PostField> fields, ContestStatus status, SortOption sortOption) {
        List<Post> posts;
        switch (sortOption) {
            case LATEST:
                posts = postJpaRepository.findByPostFieldsInAndContestStatusNotOrderByCreatedAtDesc(fields, status);
                break;
            case MOST_BOOKMARKS:
                posts = postJpaRepository.findByPostFieldsInAndContestStatusNotOrderByBookmarkCountDesc(fields, status);
                break;
            case MOST_AWAITERS:
                posts = postJpaRepository.findByPostFieldsInAndContestStatusNotOrderByAwaiterCountDesc(fields, status);
                break;
            case CLOSEST_DEADLINE:
                posts = postJpaRepository.findByPostFieldsInAndContestStatusNotOrderByEndDateAsc(fields, status);
                break;
            default:
                posts = postJpaRepository.findByPostFieldsInAndContestStatusNotOrderByCreatedAtDesc(fields, status);
        }
        return posts.stream().map(Post::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<PostDomain> findByContestStatusIn(List<ContestStatus> statuses) {
        return postJpaRepository.findByContestStatusIn(statuses).stream()
                .map(Post::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public void saveAll(List<PostDomain> posts) {
        List<Post> postEntities = posts.stream()
                .map(Post::from)
                .collect(Collectors.toList());
        postJpaRepository.saveAll(postEntities);
    }
}
