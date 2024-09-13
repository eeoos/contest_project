package core.contest5.bookmark.dto;


import core.contest5.bookmark.service.BookmarkDomain;

public record BookmarkRequest(
        Long memberId,
        Long postId
) {

    public BookmarkDomain toBookmarkDomain() {
        return new BookmarkDomain(
                memberId,
                postId
        );
    }
}
