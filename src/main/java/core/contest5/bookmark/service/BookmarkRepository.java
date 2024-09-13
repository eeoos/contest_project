package core.contest5.bookmark.service;

public interface BookmarkRepository {

    boolean isBookmarked(BookmarkDomain bookmarkDomain);

    void delete(BookmarkDomain bookmarkDomain);

    void save(BookmarkDomain bookmarkDomain);

    long count(BookmarkDomain bookmarkDomain);
}
