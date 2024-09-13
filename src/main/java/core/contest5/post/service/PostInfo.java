package core.contest5.post.service;

import core.contest5.post.domain.ContestStatus;
import core.contest5.post.domain.PostField;

import java.util.HashSet;
import java.util.Set;

public record PostInfo(
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

}
