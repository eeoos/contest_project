package core.contest5.post.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import core.contest5.member.domain.MemberResponse;
import core.contest5.post.service.PostDomain;
import core.contest5.team.domain.TeamMemberDomain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

public record PostResponse(
        Long postId,
        String title,

        Long bookmarkCount,

        Long dDay,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        LocalDateTime endDate,

        String posterImage,

        Set<PostField> postFields,
        Long awaiterCount,

        MemberResponse user

) {
    public static PostResponse from(PostDomain domain) {
        Long dDay = ChronoUnit.DAYS.between(domain.getPostInfo().startDate().toLocalDate(), domain.getPostInfo().endDate().toLocalDate());
        // 마감일이 지나면 -1 고정
        if (dDay < 0) {
            dDay = -1L;
        }
        return new PostResponse(
                domain.getId(),
                domain.getPostInfo().title(),
                domain.getBookmarkCount(),
                dDay,
                domain.getPostInfo().endDate(),
                domain.getPostInfo().posterImage(),
                domain.getPostInfo().postFields(),
                domain.getAwaiterCount(),
                MemberResponse.from(domain.getMember())
        );
    }

}
