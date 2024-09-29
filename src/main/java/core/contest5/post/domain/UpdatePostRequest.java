package core.contest5.post.domain;


import core.contest5.post.service.PostInfo;
import core.contest5.post.service.UpdatedPostInfo;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@Getter
public class UpdatePostRequest{
//    private Long id;
    private String title;
    private String contentText;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String qualification;
    private String awardScale;
    private String host;
    private ApplicationMethod applicationMethod;

    @Email
    private String applicationEmail;
    private String hostHomepageURL;
    private Set<PostField> postFields;
    private Long userId;



    public UpdatedPostInfo toUpdatedPost(Long postId, String posterImage, List<String> attachedFiles, PostContent content) {

        ContestStatus status;
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(startDate)) {
            status = ContestStatus.NOT_STARTED; // 접수 전
        } else if (now.isAfter(endDate)) {
            status = ContestStatus.CLOSED; // 접수 끝남
        } else {
            status = ContestStatus.IN_PROGRESS; // 접수 중
        }
        return new UpdatedPostInfo(

                postId,
                new PostInfo(
                        title,
                        content,
                        startDate,
                        endDate,
                        posterImage,
                        attachedFiles,
                        qualification,
                        awardScale,
                        host,
                        applicationMethod,
                        applicationEmail,
                        hostHomepageURL,
                        postFields,
                        status
                ),
                userId
        );
    }
}
