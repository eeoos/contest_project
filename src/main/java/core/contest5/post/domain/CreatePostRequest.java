package core.contest5.post.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import core.contest5.post.service.PostInfo;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@Getter
public class CreatePostRequest{
    private String title;
    private String contentText;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDateTime endDate;
    private String qualification;
    private String awardScale;
    private String host;
    private ApplicationMethod applicationMethod;

    @Email
    private String applicationEmail;
    private String hostHomepageURL;
    private Set<PostField> postFields;


    public PostInfo toPostInfo(String posterImage, List<String> attachedFiles, PostContent content) {

        ContestStatus status;
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(startDate)) {
            status = ContestStatus.NOT_STARTED; // 접수 전
        } else if (now.isAfter(endDate)) {
            status = ContestStatus.CLOSED; // 접수 끝남
        } else {
            status = ContestStatus.IN_PROGRESS; // 접수 중
        }

        return new PostInfo(
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
