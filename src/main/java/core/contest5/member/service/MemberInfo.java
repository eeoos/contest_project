package core.contest5.member.service;

import core.contest5.member.domain.memberinfo.*;
import jakarta.validation.constraints.Email;

import java.util.List;
import java.util.Set;

public record MemberInfo(
        @Email
        String email,
        String name,
        String profileImage,

        MemberRole memberRole,

        MemberField memberField,

        MemberDuty memberDuty,
        Grade grade,
        String school,
        String major,

        Set<TechStack> techStacks,
        Set<Certificate> certificates,
        Set<String> awards,
        Set<ContestEntry> contestEntries

) {
}
