package core.contest5.post.service;


import core.contest5.member.service.MemberDomain;
import core.contest5.post.domain.ContestStatus;
import core.contest5.post.domain.PostField;
import core.contest5.post.domain.SortOption;

import java.util.List;
import java.util.Set;

public interface PostRepository {

    Long save(PostInfo info, MemberDomain user);
    void incrementViewCount(Long postId);

    void incrementBookmarkCount(Long postId);

    void decrementBookmarkCount(Long postId);
//    void incrementAwaiterCount(Long postId);
//    void decrementAwaiterCount(Long postId);
    PostDomain findById(Long postId);

    void update(PostDomain domain);
    void delete(Long postId);

    List<PostDomain> searchPosts(String keyword);

    PostDomain save(PostDomain postDomain);

    List<PostDomain> findAllExceptStatusSorted(ContestStatus status, SortOption sortOption);
    List<PostDomain> findByPostFieldsInAndContestStatusNotSorted(Set<PostField> fields, ContestStatus status, SortOption sortOption);
    List<PostDomain> findByContestStatusIn(List<ContestStatus> statuses);
    void saveAll(List<PostDomain> posts);
}
