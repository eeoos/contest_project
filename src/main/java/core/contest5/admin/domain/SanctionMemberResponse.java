package core.contest5.admin.domain;

import core.contest5.member.domain.memberinfo.Certificate;
import core.contest5.member.domain.memberinfo.TechStack;
import core.contest5.member.service.MemberDomain;

import java.util.List;
import java.util.stream.Collectors;

public record SanctionMemberResponse(
         Long memberId,

        String memberName,
        String profileImage,
        String field,
        String duty,
        List<String> techStacks,
        List<String> certificates
){
    public static SanctionMemberResponse from(MemberDomain domain) {
        return new SanctionMemberResponse(
                domain.getId(),
                domain.getMemberInfo().name(),
                domain.getMemberInfo().profileImage(),
                domain.getMemberInfo().memberField().getFieldName(),
                domain.getMemberInfo().memberDuty().getDutyName(),
                domain.getMemberInfo().techStacks().stream().map(TechStack::getName).collect(Collectors.toList()),
                domain.getMemberInfo().certificates().stream().map(Certificate::getName).collect(Collectors.toList())
        );
    }
}
