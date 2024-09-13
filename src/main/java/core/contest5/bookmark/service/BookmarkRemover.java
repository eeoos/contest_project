package core.contest5.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookmarkRemover {

    private final BookmarkRepository bookmarkRepository;

    public void remove(BookmarkDomain bookmarkDomain) {
        bookmarkRepository.delete(bookmarkDomain);
    }

}
