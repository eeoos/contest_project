package core.contest5.post.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import core.contest5.member.domain.MemberResponse;
import core.contest5.post.service.PostDomain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record CommonPostDetailsResponse(
        Long postId,
        String title,

        Long viewCount,

        Long bookmarkCount,

//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
//        LocalDateTime startDate,
        Long dDay,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        LocalDateTime endDate,

        String posterImage,
//        List<String> attachedFile,

        String qualification,

        String awardScale,

        String host,

        String applicationMethod,
        String applicationEmail,

        String hostHomepageURL,

        List<String> postFields,
        ContestStatus contestStatus,

        MemberResponse user

) {
    public static CommonPostDetailsResponse from(PostDomain domain) {

        Long dDay = ChronoUnit.DAYS.between(domain.getPostInfo().startDate().toLocalDate(), domain.getPostInfo().endDate().toLocalDate());
        // 마감일이 지나면 -1 고정
        if (dDay < 0) {
            dDay = -1L;
        }

        return new CommonPostDetailsResponse(
                domain.getId(),
                domain.getPostInfo().title(),
                domain.getViewCount(),
                domain.getBookmarkCount(),
                dDay,
                domain.getPostInfo().endDate(),
                domain.getPostInfo().posterImage(),
//                Optional.ofNullable(domain.getPostInfo().attachedFiles()).orElse(Collections.emptyList()),
                domain.getPostInfo().qualification(),
                domain.getPostInfo().awardScale(),
                domain.getPostInfo().host(),
                domain.getPostInfo().applicationMethod().getDescription(),
                domain.getPostInfo().applicationEmail(),
                domain.getPostInfo().hostHomepageURL(),
                domain.getPostInfo().postFields().stream().map(PostField::getDescription).collect(Collectors.toList()),
                domain.getPostInfo().contestStatus(),
                MemberResponse.from(domain.getMember())
        );
    }
}
