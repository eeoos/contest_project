package core.contest5.member.service;

import core.contest5.member.domain.MemberRole;
import jakarta.validation.constraints.Email;

public record MemberInfo (
        @Email
        String email,
        String name,
        String profileImage,

        MemberRole memberRole
) {
}
