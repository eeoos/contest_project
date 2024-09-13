package core.contest5.post.domain;

import core.contest5.member.domain.MemberResponse;
import core.contest5.post.service.PostDomain;

import java.util.Set;

public record PostDetailsResponse(
        Long postId,
        String title,

        String content,

        Long viewCount,

        Long bookmarkCount,

        String startDate,

        String endDate,

        String posterImage,

        String qualification,

        String awardScale,

        String host,

        String hostHomepageURL,

        Set<PostField> postFields,
        ContestStatus contestStatus,

        MemberResponse user

) {
    public static PostDetailsResponse from(PostDomain domain) {
        return new PostDetailsResponse(
                domain.getId(),
                domain.getPostInfo().title(),
                domain.getPostInfo().content(),
                domain.getViewCount(),
                domain.getBookmarkCount(),
                domain.getPostInfo().startDate(),
                domain.getPostInfo().endDate(),
                domain.getPostInfo().posterImage(),
                domain.getPostInfo().qualification(),
                domain.getPostInfo().awardScale(),
                domain.getPostInfo().host(),
                domain.getPostInfo().hostHomepageURL(),
                domain.getPostInfo().postFields(),
                domain.getPostInfo().contestStatus(),
                MemberResponse.from(domain.getMember())
        );
    }
}
