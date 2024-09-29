package core.contest5.admin.service;

import core.contest5.admin.domain.SanctionType;
import core.contest5.member.service.MemberDomain;
import core.contest5.member.service.MemberRepository;
import core.contest5.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SanctionService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public Long getDailyNewSignups() {
        return memberService.getTodaySignUpCount();
    }

    public MemberDomain getMemberById(Long memberId) {
        return memberService.readMember(memberId);
    }

    @Transactional
    public void applySanction(Long memberId, SanctionType sanctionType) {

        MemberDomain memberDomain = getMemberById(memberId);

        if (sanctionType == SanctionType.WARNING) {
            memberDomain.increaseWarningCount();
        } else {
            memberDomain.applySuspension(sanctionType);
        }
       memberRepository.update(memberDomain);
    }
}
