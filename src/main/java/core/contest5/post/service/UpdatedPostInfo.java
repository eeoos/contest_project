package core.contest5.post.service;

public record UpdatedPostInfo(
        Long postId,
        PostInfo postInfo,
        Long memberId
){
}
