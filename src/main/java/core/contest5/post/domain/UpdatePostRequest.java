package core.contest5.post.domain;


import core.contest5.post.service.PostInfo;
import core.contest5.post.service.UpdatedPostInfo;

import java.time.LocalDateTime;
import java.util.Set;


public record UpdatePostRequest(
        Long id,
        String title,
        String content,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String posterImage,
        String qualification,
        String awardScale,
        String host,
        String hostHomepageURL,
        Set<PostField> postFields,
        ContestStatus contestStatus,

        Long userId
) {
    public UpdatedPostInfo toUpdatedPost(Long postId) {
        return new UpdatedPostInfo(
                postId,
                new PostInfo(
                        title,
                        content,
                        startDate,
                        endDate,
                        posterImage,
                        qualification,
                        awardScale,
                        host,
                        hostHomepageURL,
                        postFields,
                        contestStatus
                ),
                userId
        );
    }
}
