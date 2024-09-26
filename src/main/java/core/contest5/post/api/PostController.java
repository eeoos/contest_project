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
            @AuthenticationPrincipal MemberDomain memberDomain) throws IOException {
        String savedPosterImageFileName = fileService.saveFile(posterImage, "poster");
        List<String> savedAttachedFileNames = new ArrayList<>();
        if (attachedFiles != null && !attachedFiles.isEmpty()) {
            savedAttachedFileNames = fileService.saveFiles(attachedFiles, "attachment");
        }
        Long createdPostId = postService.writePost(postRequest.toPostInfo(savedPosterImageFileName, savedAttachedFileNames), memberDomain.getId());
        return ResponseEntity.ok(createdPostId);
    }



    @GetMapping("/posts/{postId}")
    public ResponseEntity<CommonPostDetailsResponse> readCommonPostDetails(@PathVariable Long postId) {
        PostDomain post = postService.readPost(postId);
        return ResponseEntity.ok(CommonPostDetailsResponse.from(post));
    }

    /*@GetMapping("/posts/{postId}/details")
    public ResponseEntity<PostDetailsContentResponse> readPostDetailsContent(@PathVariable Long postId) {
    }*/

    @GetMapping("/posts/fields")
    public ResponseEntity<List<String>> getFields() {
        List<String> fieldDescriptions = Arrays.stream(PostField.values())
                .map(PostField::getDescription)
                .collect(Collectors.toList());
        return ResponseEntity.ok(fieldDescriptions);
    }

    @PutMapping(value = "/posts/{postId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @RequestPart("updatePostRequest") UpdatePostRequest updatePostRequest,
            @RequestPart(value = "posterImage", required = false) MultipartFile posterImage,
            @RequestPart(value = "attachedFiles", required = false) List<MultipartFile> attachedFiles
    ) throws IOException {

        PostDomain existingPost = postService.readPost(postId);

        String savedPosterImageName = existingPost.getPostInfo().posterImage();
        if (posterImage != null && !posterImage.isEmpty()) {
            savedPosterImageName = fileService.saveFile(posterImage, "poster");
        }

        List<String> savedAttachedFileNames = new ArrayList<>(existingPost.getPostInfo().attachedFiles());
        if (attachedFiles != null && !attachedFiles.isEmpty()) {
            savedAttachedFileNames = fileService.saveFiles(attachedFiles, "attachment");
        }

        postService.updatePost(updatePostRequest.toUpdatedPost(postId, savedPosterImageName, savedAttachedFileNames));
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

    /*@GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getPosts(
            @RequestParam(required = false) Set<PostField> fields

    ) {
        List<PostDomain> posts;
        if (fields != null && !fields.isEmpty()) {
            posts = postService.getPostsByFields(fields);
        } else {
            posts = postService.getPosts();
        }
        return ResponseEntity.ok(posts.stream()
                .map(PostResponse::from)
                .collect(Collectors.toList()));
    }


    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getPosts() {
        List<PostDomain> posts = postService.getPosts();
        return ResponseEntity.ok(posts.stream()
                .map(PostResponse::from)
                .collect(Collectors.toList()));

    }

    @GetMapping("/posts/fields")
    public ResponseEntity<List<PostResponse>> getPostsByFields(@RequestParam Set<PostField> fields) {
        List<PostDomain> postsByFields = postService.getPostsByFields(fields);
        return ResponseEntity.ok(postsByFields.stream()
                .map(PostResponse::from)
                .collect(Collectors.toList()));

    }*/
    /*@PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createPost(
            @RequestPart CreatePostRequest postRequest,
            @RequestPart MultipartFile posterImage,

            @AuthenticationPrincipal MemberDomain memberDomain) throws IOException {
        String savedFileName = fileService.saveFile(posterImage, "poster");
        Long createdPostId = postService.writePost(postRequest.toPostInfo(savedFileName), memberDomain.getId());
        return ResponseEntity.ok(createdPostId);
    }*/
}
