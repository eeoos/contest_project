package core.contest5.bookmark.service;

import core.contest5.bookmark.dto.BookmarkStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkAppender bookmarkAppender;
    private final BookmarkRemover bookmarkRemover;
    private final BookmarkReader bookmarkReader;

    public BookmarkStatus flipBookmark(BookmarkDomain bookmarkDomain) {
        if (bookmarkReader.isBookmarked(bookmarkDomain)) {
            bookmarkRemover.remove(bookmarkDomain);
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
