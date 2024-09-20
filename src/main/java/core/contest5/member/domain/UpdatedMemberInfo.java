package core.contest5.member.domain;

import core.contest5.member.service.MemberInfo;

public record UpdatedMemberInfo(
        Long memberId,
        MemberInfo memberInfo
) {
}
