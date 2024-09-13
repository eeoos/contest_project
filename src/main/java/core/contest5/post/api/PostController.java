package core.contest5.post.api;

import core.contest5.member.service.MemberDomain;
import core.contest5.post.domain.*;
import core.contest5.post.service.PostDomain;
import core.contest5.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contest")
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<Long> createPost(@RequestBody CreatePostRequest postRequest,
                                           @AuthenticationPrincipal MemberDomain memberDomain) {
        Long createdPostId = postService.writePost(postRequest.toPostInfo(), memberDomain.id());
        return ResponseEntity.ok(createdPostId);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDetailsResponse> readPost(@PathVariable Long postId) {
        PostDomain post = postService.readPost(postId);
        return ResponseEntity.ok(PostDetailsResponse.from(post));
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest updatePostRequest) {
        postService.updatePost(updatePostRequest.toUpdatedPost(postId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
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

    }
}
