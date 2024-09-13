package core.contest5.member.domain;


import core.contest5.member.service.MemberInfo;

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
                MemberRole.GUEST
        );
    }
}
