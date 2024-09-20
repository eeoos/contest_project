package core.contest5.post.service;

import core.contest5.member.service.MemberDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionValidator {

    public void validate(PostDomain postDomain, MemberDomain member) {
        if (!postDomain.getMember().getId().equals(member.getId())) {
            throw new RuntimeException("Permission denied");
        }
    }
}
