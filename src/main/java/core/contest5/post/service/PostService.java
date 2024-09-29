package core.contest5.post.service;

import core.contest5.file.FileService;
import core.contest5.global.exception.NoSearchResultsException;
import core.contest5.member.service.MemberDomain;
import core.contest5.member.service.MemberReader;
import core.contest5.post.domain.ContestStatus;
import core.contest5.post.domain.PostField;
import core.contest5.post.domain.PostResponse;
import core.contest5.post.domain.SortOption;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private final FileService fileService;

    public Long writePost(PostInfo postInfo, Long userId) {
        MemberDomain member = memberReader.read(userId);

        // ContestStatus 결정
        ContestStatus initialStatus = determineInitialStatus(postInfo.startDate(), postInfo.endDate());

        PostInfo updatedPostInfo = new PostInfo(
                postInfo.title(),
                postInfo.content(),
                postInfo.startDate(),
                postInfo.endDate(),
                postInfo.posterImage(),
                postInfo.attachedFiles(),
                postInfo.qualification(),
                postInfo.awardScale(),
                postInfo.host(),
                postInfo.applicationMethod(),
                postInfo.applicationEmail(),
                postInfo.hostHomepageURL(),
                postInfo.postFields(),
                initialStatus
        );

        return postAppender.append(updatedPostInfo, member);
    }

    public PostDomain readPost(Long postId) {
        return postReader.read(postId);
    }

    public String readPostApplicationMethod(Long postId) {
        PostDomain postDomain = postReader.read(postId);
        switch (postDomain.getPostInfo().applicationMethod()) {
            case EMAIL : return postDomain.getPostInfo().applicationEmail();
            case HOMEPAGE : return postDomain.getPostInfo().hostHomepageURL();
//            case QR: return postDomain.getPostInfo().applicationMethod().getDescription();
            default : return postDomain.getPostInfo().hostHomepageURL();
        }
    }

    /*public List<reviewDomain> readPostReviews(Long postId) { // 팁/후기 - 커뮤니티와 결합 후 구현
        List<reviewDomain> reviews = reviewRepository.findByPostId(postId);
        return reviews;
    }*/

    @Transactional
    public void updatePost(UpdatedPostInfo updatedInfo) throws IOException {
        MemberDomain member = memberReader.read(updatedInfo.memberId());
        PostDomain existingPost = postReader.read(updatedInfo.postId());
        permissionValidator.validate(existingPost, member);

        // 새 포스터 이미지가 있고, 기존 이미지와 다른 경우에만 파일 삭제 및 업데이트
        if (updatedInfo.postInfo().posterImage() != null &&
                !updatedInfo.postInfo().posterImage().equals(existingPost.getPostInfo().posterImage())) {
            fileService.deleteFile(existingPost.getPostInfo().posterImage(), "poster");
        }

        // 첨부 파일 처리
        List<String> existingAttachedFiles = existingPost.getPostInfo().attachedFiles();
        List<String> updatedAttachedFiles = updatedInfo.postInfo().attachedFiles();

        if (updatedAttachedFiles != null && !updatedAttachedFiles.equals(existingAttachedFiles)) {
            // 기존 파일 삭제
            if (existingAttachedFiles != null) {
                fileService.deleteFiles(existingAttachedFiles, "attachment");
            }
        }

        postUpdator.update(existingPost.getId(), updatedInfo.postInfo());
    }

    public void deletePost(Long postId) {
        postRepository.delete(postId);
    }

    public List<PostDomain> getActivePosts(Set<PostField> fields, SortOption sortOption) {
        if (fields != null && !fields.isEmpty()) {
            return postRepository.findByPostFieldsInAndContestStatusNotSorted(fields, ContestStatus.CLOSED, sortOption);
        } else {
            return postRepository.findAllExceptStatusSorted(ContestStatus.CLOSED, sortOption);
        }
    }


    public List<PostDomain> searchPosts(String keyword) {
        List<PostDomain> searchResults = postRepository.searchPosts(keyword);
        // 여기서 반환되는 결과는 이미 CLOSED 상태의 게시글이 제외된 상태
        if (searchResults.isEmpty()) {
            throw new NoSearchResultsException("검색 결과가 없습니다.");
        }
        return searchResults;
    }
    private ContestStatus determineInitialStatus(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startDate)) {
            return ContestStatus.NOT_STARTED;
        } else if (now.isAfter(endDate)) {
            return ContestStatus.CLOSED;
        } else {
            return ContestStatus.IN_PROGRESS;
        }
    }
}
