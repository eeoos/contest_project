package core.contest5.post.domain;

import core.contest5.member.domain.MemberResponse;
import core.contest5.post.service.PostDomain;

import java.util.Set;

public record PostResponse(
        Long postId,
        String title,

        Long bookmarkCount,

        String startDate,

        String endDate,

        String posterImage,

        Set<PostField> postFields,

        MemberResponse user

) {
    public static PostResponse from(PostDomain domain) {
        return new PostResponse(
                domain.getId(),
                domain.getPostInfo().title(),
                domain.getBookmarkCount(),
                domain.getPostInfo().startDate(),
                domain.getPostInfo().endDate(),
                domain.getPostInfo().posterImage(),
                domain.getPostInfo().postFields(),
                MemberResponse.from(domain.getMember())
        );
    }
}
