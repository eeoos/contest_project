package core.contest5.post.service;

import core.contest5.member.service.MemberDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostAppender {

    private final PostRepository postRepository;

    public Long append(PostInfo postInfo, MemberDomain user) {
        return postRepository.save(postInfo, user);
    }
}
