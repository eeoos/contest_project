package core.contest5.member.domain;

import core.contest5.member.domain.memberinfo.*;
import core.contest5.member.service.MemberDomain;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record MemberResponse(
        Long id,
        String email,
        String name,
        String profileImage,
        MemberRole role,
        MemberField memberField,
        MemberDuty memberDuty,
        Grade grade,
        String school,
        String major,
        Set<TechStack> techStacks,
        Set<Certificate> certificates,
        Set<ContestEntry> contestEntries,
        Set<Award> awards,
        LocalDateTime createdAt,
        LocalDateTime updatedAt


) {
    public static MemberResponse from(MemberDomain domain) {
        return new MemberResponse(
                domain.getId(),
                domain.getMemberInfo().email(),
                domain.getMemberInfo().name(),
                domain.getMemberInfo().profileImage(),
                domain.getMemberInfo().memberRole(),
                domain.getMemberInfo().memberField(),
                domain.getMemberInfo().memberDuty(),
                domain.getMemberInfo().grade(),
                domain.getMemberInfo().school(),
                domain.getMemberInfo().major(),
                domain.getMemberInfo().techStacks(),
                domain.getMemberInfo().certificates(),
                domain.getMemberInfo().contestEntries(),
                domain.getMemberInfo().awards(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}
