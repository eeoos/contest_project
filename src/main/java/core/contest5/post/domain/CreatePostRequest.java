package core.contest5.post.domain;


import core.contest5.post.service.PostInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;


@AllArgsConstructor
@Getter
public class CreatePostRequest{
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String qualification;
    private String awardScale;
    private String host;
    private String hostHomepageURL;
    private Set<PostField> postFields;

    private ContestStatus contestStatus;

    public PostInfo toPostInfo(String posterImage) {
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

    /*public PostInfo toPostInfo() {
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
    }*/
}
