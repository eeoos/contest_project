package core.contest5.post.api;

import core.contest5.file.FileService;
import core.contest5.global.exception.NoSearchResultsException;
import core.contest5.member.service.MemberDomain;
import core.contest5.post.domain.*;
import core.contest5.post.service.PostDomain;
import core.contest5.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contest")
public class PostController {

    private final PostService postService;
    private final FileService fileService;

    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createPost(
            @RequestPart CreatePostRequest postRequest,
            @RequestPart MultipartFile posterImage, //필수O, 없으면 회사 로고라도
            @RequestPart(required = false) List<MultipartFile> attachedFiles, //필수X
            @RequestPart(required = false) List<MultipartFile> contentImages,
//            @RequestPart MultipartFile contentText,
            @AuthenticationPrincipal MemberDomain memberDomain) throws IOException {
        String savedPosterImageFileName = fileService.saveFile(posterImage, "poster");

        List<String> savedContentImageFileNames = saveMultipartFiles(contentImages, "content");
        List<String> savedAttachedFileNames = saveMultipartFiles(attachedFiles, "attachment");

        PostContent postContent = new PostContent(postRequest.getContentText(), savedContentImageFileNames);

        Long createdPostId = postService.writePost(
                postRequest.toPostInfo(savedPosterImageFileName, savedAttachedFileNames, postContent),
                memberDomain.getId());

        return ResponseEntity.ok(createdPostId);
    }

    @GetMapping("/posts/{postId}") //포스터
    public ResponseEntity<CommonPostDetailsResponse> readCommonPostDetails(@PathVariable Long postId) {
        PostDomain post = postService.readPost(postId);
        return ResponseEntity.ok(CommonPostDetailsResponse.from(post));
    }

    @GetMapping("/posts/{postId}/details")
    public ResponseEntity<PostDetailsContentResponse> readPostDetailsContent(@PathVariable Long postId) {
        PostDomain post = postService.readPost(postId);
        return ResponseEntity.ok(PostDetailsContentResponse.from(post));
    }

    /*@GetMapping("/posts/{postId}/reviews") // 팁/후기 - 커뮤니티와 결합 후 구현
    public ResponseEntity<List<PostReviewsResponse>> readPostReviews(@PathVariable Long postId) {
        List<reviewDomain> reviews = postService.readPostReviews(postId);
        return ResponseEntity.ok(PostReviewsResponse.from(reviewDomain));
    }*/


    @GetMapping("/posts/fields") //관심 목록
    public ResponseEntity<List<String>> getFields() {
        List<String> fieldDescriptions = Arrays.stream(PostField.values())
                .map(PostField::getDescription)
                .collect(Collectors.toList());
        return ResponseEntity.ok(fieldDescriptions);
    }

    @GetMapping("/posts/sortOption") //정렬 옵션
    public ResponseEntity<List<String>> getSortOptions() {
        List<String> sortOptions = Arrays.stream(SortOption.values())
                .map(SortOption::getDescription)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sortOptions);
    }



    @PutMapping(value = "/posts/{postId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @RequestPart("updatePostRequest") UpdatePostRequest updatePostRequest,
            @RequestPart(value = "posterImage", required = false) MultipartFile posterImage,
            @RequestPart(value = "attachedFiles", required = false) List<MultipartFile> attachedFiles,
            @RequestPart(value = "contentImages", required = false) List<MultipartFile> contentImages
//            @RequestPart String contentText
    ) throws IOException {

        PostDomain existingPost = postService.readPost(postId);

        String savedPosterImageName = updateFileIfProvided(posterImage, existingPost.getPostInfo().posterImage(), "poster");

        List<String> savedAttachedFileNames = updateFilesIfProvided(attachedFiles, existingPost.getPostInfo().attachedFiles(), "attachment");
        List<String> savedContentImageNames = updateFilesIfProvided(contentImages, existingPost.getPostInfo().content().getImageUrls(), "content");

        PostContent postContent = new PostContent(updatePostRequest.getContentText(), savedContentImageNames);

        postService.updatePost(updatePostRequest.toUpdatedPost(postId, savedPosterImageName, savedAttachedFileNames, postContent));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) { // closed 상태는 삭제 불가
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getPosts(
            @RequestParam(required = false) Set<PostField> fields,
            @RequestParam(required = false, defaultValue = "LATEST") SortOption sortOption) {
        List<PostDomain> posts = postService.getActivePosts(fields, sortOption);
        return ResponseEntity.ok(posts.stream()
                .map(PostResponse::from)
                .collect(Collectors.toList()));
    }

    @GetMapping("/posts/{postId}/application/detail") //지원하기
    public ResponseEntity<ApplicationResponse> getApplicationDetail(
            @PathVariable Long postId) {
        PostDomain postDomain = postService.readPost(postId);
        return ResponseEntity.ok(ApplicationResponse.from(postDomain));
    }

    @GetMapping("/posts/{postId}/application")
    public ResponseEntity<String> getApplicationMethod(@PathVariable Long postId) {
        PostDomain postDomain = postService.readPost(postId);
        return ResponseEntity.ok(postDomain.getPostInfo().applicationMethod().getDescription());
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(@RequestParam String keyword) {
        try {
            List<PostResponse> searchResults = postService.searchPosts(keyword)
                    .stream()
                    .map(PostResponse::from)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(searchResults);
        } catch (NoSearchResultsException e) {
            return ResponseEntity.ok(Collections.singletonMap("message", e.getMessage()));
        }
    }


    private List<String> saveMultipartFiles(List<MultipartFile> files, String fileType) throws IOException {
        if (files == null || files.isEmpty()) {
            return new ArrayList<>();
        }
        return fileService.saveFiles(files, fileType);
    }

    private String updateFileIfProvided(MultipartFile newFile, String existingFileName, String fileType) throws IOException {
        if (newFile != null && !newFile.isEmpty()) {
            if (existingFileName != null) {
                fileService.deleteFile(existingFileName, fileType);
            }
            return fileService.saveFile(newFile, fileType);
        }
        return existingFileName;
    }



    private List<String> updateFilesIfProvided(List<MultipartFile> newFiles, List<String> existingFileNames, String fileType) throws IOException {
        if (newFiles != null && !newFiles.isEmpty()) {
            if (existingFileNames != null && !existingFileNames.isEmpty()) {
                fileService.deleteFiles(existingFileNames, fileType);
            }
            return fileService.saveFiles(newFiles, fileType);
        }
        return new ArrayList<>(existingFileNames != null ? existingFileNames : Collections.emptyList());
    }
}
