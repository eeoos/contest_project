package core.contest5.bookmark.service;

import core.contest5.post.service.PostDomain;
import core.contest5.post.service.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BookmarkAppender {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;

    @Transactional
    public void append(BookmarkDomain bookmarkDomain) {
        bookmarkRepository.save(bookmarkDomain);
        PostDomain postDomain = postRepository.findById(bookmarkDomain.postId());
        postDomain.increaseBookmarkCount();
        postRepository.incrementBookmarkCount(bookmarkDomain.postId());
    }
}
