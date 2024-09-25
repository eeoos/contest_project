package core.contest5.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PostUpdator {
    private final PostRepository postRepository;

    public void update(PostDomain postDomain) {
        postRepository.update(postDomain);
    }

    @Transactional
    public void update(Long postId, PostInfo updatedPostInfo) {
        PostDomain postDomain = postRepository.findById(postId);
        if (postDomain == null) {
            throw new IllegalArgumentException("Post not found");
        }

        // PostDomain 객체 업데이트
        PostDomain updatedPostDomain = new PostDomain(
                postDomain.getId(),
                updatedPostInfo,
                postDomain.getViewCount(),
                postDomain.getBookmarkCount(),
                postDomain.getMember(),
                postDomain.getCreatedAt(),
                LocalDateTime.now()
        );

        postRepository.update(updatedPostDomain);

    }

}
