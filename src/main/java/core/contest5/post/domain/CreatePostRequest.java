package core.contest5.post.domain;


import core.contest5.post.service.PostInfo;

import java.util.Set;


public record CreatePostRequest(
        String title,
        String content,
        String startDate,
        String endDate,

        String posterImage,
        String qualification,
        String awardScale,
        String host,
        String hostHomepageURL,
        Set<PostField> postFields,

        ContestStatus contestStatus


) {
    public PostInfo toPostInfo() {
        return new PostInfo(
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
        );
    }
}
