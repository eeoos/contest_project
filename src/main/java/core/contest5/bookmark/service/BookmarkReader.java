package core.contest5.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookmarkReader {

    private final BookmarkRepository bookmarkRepository;

    public boolean isBookmarked(BookmarkDomain bookmarkDomain) {
        return bookmarkRepository.isBookmarked(bookmarkDomain);
    }

    public long count(BookmarkDomain bookmarkDomain) {

        return bookmarkRepository.count(bookmarkDomain);
    }

}
