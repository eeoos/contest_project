package core.contest5.member.domain;

import core.contest5.member.service.MemberDomain;
import lombok.Builder;

@Builder
public record MemberResponse(
        Long id,
        String email,
        String name,
        String profileImage,
        MemberRole role
) {
    public static MemberResponse from(MemberDomain domain) {
        return new MemberResponse(
                domain.id(),
                domain.info().email(),
                domain.info().name(),
                domain.info().profileImage(),
                domain.info().memberRole()
        );
    }
}
