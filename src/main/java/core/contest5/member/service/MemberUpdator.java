package core.contest5.member.service;

import core.contest5.member.domain.UpdatedMemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberUpdator {

    private final MemberRepository memberRepository;

    public void update(MemberDomain memberDomain, UpdatedMemberInfo updatedMemberInfo) {
        memberDomain.update(updatedMemberInfo);
        memberRepository.update(memberDomain);
    }
    /*public void update(MemberDomain memberDomain) {
        memberDomain.update(memberDomain);

        memberRepository.update(memberDomain);
    }*/
}
