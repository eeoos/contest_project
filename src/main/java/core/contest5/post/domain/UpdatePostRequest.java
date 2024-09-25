package core.contest5.post.domain;


import core.contest5.post.service.PostInfo;
import core.contest5.post.service.UpdatedPostInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;


@AllArgsConstructor
@Getter
public class UpdatePostRequest{
    private Long id;
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
    private Long userId;



    public UpdatedPostInfo toUpdatedPost(Long postId, String posterImage) {
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
