package core.contest5.post.service;

import core.contest5.post.domain.ApplicationMethod;
import core.contest5.post.domain.ContestStatus;
import core.contest5.post.domain.PostContent;
import core.contest5.post.domain.PostField;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record PostInfo(
        String title,
        PostContent content,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String posterImage,
        List<String> attachedFiles,
        String qualification,
        String awardScale,
        String host,
        ApplicationMethod applicationMethod,
        String applicationEmail,
        String hostHomepageURL,
        Set<PostField> postFields,
        ContestStatus contestStatus
) {
}
