package core.contest5.post.service;


import core.contest5.member.service.MemberDomain;
import core.contest5.post.domain.PostField;

import java.util.List;
import java.util.Set;

public interface PostRepository {
    Long save(PostInfo info, MemberDomain user);
    void incrementViewCount(Long postId);

    void incrementBookmarkCount(Long postId);

    PostDomain findById(Long postId);

    void update(PostDomain domain);
    void delete(Long postId);

    List<PostDomain> findAll();

    List<PostDomain> findByPostFieldsIn(Set<PostField> fields);


}
