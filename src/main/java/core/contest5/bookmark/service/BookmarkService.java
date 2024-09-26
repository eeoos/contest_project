package core.contest5.bookmark.service;

import core.contest5.bookmark.dto.BookmarkStatus;
import core.contest5.post.service.PostDomain;
import core.contest5.post.service.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkAppender bookmarkAppender;
    private final BookmarkRemover bookmarkRemover;
    private final BookmarkReader bookmarkReader;
    private final PostRepository postRepository;

    @Transactional
    public BookmarkStatus flipBookmark(BookmarkDomain bookmarkDomain) {
        if (bookmarkReader.isBookmarked(bookmarkDomain)) {
            bookmarkRemover.remove(bookmarkDomain);
            PostDomain postDomain = postRepository.findById(bookmarkDomain.postId());
            postDomain.decreaseBookmarkCount(); // bookmarkRepository.count()로 대체해도 되는지?
            postRepository.decrementBookmarkCount(bookmarkDomain.postId());
            return BookmarkStatus.UNBOOKMARK;
        }
        bookmarkAppender.append(bookmarkDomain);
        return BookmarkStatus.BOOKMARK;
    }

    public boolean isBookmarked(BookmarkDomain bookmarkDomain) {
        return bookmarkReader.isBookmarked(bookmarkDomain);
    }

    public long count(BookmarkDomain bookmarkDomain) {
        return bookmarkReader.count(bookmarkDomain);
    }
}
