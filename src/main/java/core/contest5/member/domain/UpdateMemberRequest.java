package core.contest5.member.domain;

import core.contest5.member.domain.memberinfo.Grade;

import java.util.Set;

public record UpdateMemberRequest(
//        String email,
        String profileImage,
        String name,
        String memberField,
        String memberDuty,
        Grade grade,
        String school,
        String major,
        Set<String> techStacks,
        Set<String> certificates,
        Set<String> contestEntries,
        Set<String> awards
) {
    /*public UpdatedMemberInfo toUpdatedMember(Long memberId) {
        return new UpdatedMemberInfo(
                memberId,
                new MemberInfo(
                        email,
                        name,
                        profileImage,
                        memberRole,
                        memberField,
                        memberDuty,
                        grade,
                        school,
                        major
                )
        );
    }*/
}
