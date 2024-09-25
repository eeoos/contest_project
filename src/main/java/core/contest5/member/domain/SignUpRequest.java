package core.contest5.member.domain;


import core.contest5.member.domain.memberinfo.MemberRole;
import core.contest5.member.service.MemberInfo;

import java.util.Set;

    public record SignUpRequest(
        String email,

        String name,
        String profileImage
){
    public MemberInfo toMemberInfo() {
        return new MemberInfo(
                email,
                name,
                profileImage,
                MemberRole.GUEST,
                null,
                null,
                null,
                null,
                null,
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of()

        );
    }
}
