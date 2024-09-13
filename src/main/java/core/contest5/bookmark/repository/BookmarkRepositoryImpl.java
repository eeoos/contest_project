package core.contest5.bookmark.repository;

import core.contest5.bookmark.entity.Bookmark;
import core.contest5.bookmark.service.BookmarkDomain;
import core.contest5.bookmark.service.BookmarkRepository;
import core.contest5.member.domain.Member;
import core.contest5.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepository {

    private final BookmarkJpaRepository bookmarkJpaRepository;

    @Override
    public boolean isBookmarked(BookmarkDomain bookmarkDomain) {
        return bookmarkJpaRepository.existsByPost_IdAndMember_Id(bookmarkDomain.postId(), bookmarkDomain.memberId());
    }

    @Override
    @Transactional
    public void delete(BookmarkDomain bookmarkDomain) {
        bookmarkJpaRepository.deleteByPost_IdAndMember_Id(bookmarkDomain.postId(), bookmarkDomain.memberId());
    }

    @Override
    public long count(BookmarkDomain bookmarkDomain) {
        return bookmarkJpaRepository.countByPost_Id(bookmarkDomain.postId());
    }

    @Override
    public void save(BookmarkDomain bookmarkDomain) {

        Bookmark bookmark = Bookmark.builder()
                .member(Member.from(bookmarkDomain.memberId()))
                .post(Post.from(bookmarkDomain.postId()))
                .build();
        bookmarkJpaRepository.save(bookmark);
    }
}
