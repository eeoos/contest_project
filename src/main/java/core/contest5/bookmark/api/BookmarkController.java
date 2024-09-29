package core.contest5.bookmark.api;

import core.contest5.bookmark.dto.BookmarkRequest;
import core.contest5.bookmark.dto.BookmarkStatus;
import core.contest5.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/bookmarks")
    public ResponseEntity<BookmarkStatus> flipBookmark(BookmarkRequest request) {
        BookmarkStatus bookmarkStatus = bookmarkService.flipBookmark(request.toBookmarkDomain());
        return ResponseEntity.ok(bookmarkStatus);
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<Boolean> isBookmarked(BookmarkRequest request) {
        boolean isBookmarked = bookmarkService.isBookmarked(request.toBookmarkDomain());
        return ResponseEntity.ok(isBookmarked);
    }


    @GetMapping("/bookmarks/count")
    public ResponseEntity<Long> count(BookmarkRequest request) {
        return ResponseEntity.ok(bookmarkService.count(request.toBookmarkDomain()));
    }
}
