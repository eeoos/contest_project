package core.contest5.post.service;

import core.contest5.global.exception.NoSearchResultsException;
import core.contest5.member.service.MemberDomain;
import core.contest5.member.service.MemberReader;
import core.contest5.post.domain.PostField;
import core.contest5.post.domain.PostResponse;
import core.contest5.post.domain.SortOption;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostAppender postAppender;
    private final MemberReader memberReader;
    private final PostReader postReader;
    private final PostUpdator postUpdator;
    private final PostRepository postRepository;
    private final PermissionValidator permissionValidator;
    public Long writePost(PostInfo postInfo, Long userId) {
        MemberDomain member = memberReader.read(userId);
        return postAppender.append(postInfo, member);
    }

    public PostDomain readPost(Long postId) {
        return postReader.read(postId);
    }


    public void updatePost(UpdatedPostInfo updatedInfo){
        MemberDomain member = memberReader.read(updatedInfo.memberId());
        PostDomain post = postReader.read(updatedInfo.postId());
        permissionValidator.validate(post, member);
        postUpdator.update(post, updatedInfo);
    }
    public void deletePost(Long postId) {
        postRepository.delete(postId);
    }
    public List<PostDomain> getPosts(Set<PostField> fields, SortOption sortOption) {
        if (fields != null && !fields.isEmpty()) {
            return postRepository.findByPostFieldsIn(fields, sortOption);
        } else {
            return postRepository.findAllSorted(sortOption);
        }
    }

    public List<PostDomain> searchPosts(String keyword) {
        List<PostDomain> searchResults = postRepository.searchPosts(keyword);
        if (searchResults.isEmpty()) {
            throw new NoSearchResultsException("검색 결과가 없습니다.");
        }
        return searchResults;
    }

    /*public List<PostDomain> getPosts() {
        return postRepository.findAll();

    }

    public List<PostDomain> getPostsByFields(Set<PostField> fields) {
        return postRepository.findByPostFieldsIn(fields);
//        return postDomainListByFields.stream()
//                .map(PostResponse::from)
//                .collect(Collectors.toList());
    }*/
}
