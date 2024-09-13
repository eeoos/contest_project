package core.contest5.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PostReader {

    private final PostRepository postRepository;

    @Transactional
    public PostDomain read(Long postId) {
        PostDomain domain = postRepository.findById(postId);
        domain.increaseViewCount();
        postRepository.incrementViewCount(postId);
        return domain;
    }


}
