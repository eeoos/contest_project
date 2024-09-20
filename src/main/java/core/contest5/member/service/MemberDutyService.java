package core.contest5.member.service;

import core.contest5.member.domain.memberinfo.MemberDuty;
import core.contest5.member.repository.MemberDutyRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberDutyService {
    private final MemberDutyRepository memberDutyRepository;

    public MemberDutyService(MemberDutyRepository memberDutyRepository) {
        this.memberDutyRepository = memberDutyRepository;
    }

    public MemberDuty findOrCreateMemberDuty(String dutyName) {
        return memberDutyRepository.findByDutyName(dutyName)
                .orElseGet(() -> {
                    MemberDuty newMemberDuty = new MemberDuty(dutyName);
                    return memberDutyRepository.save(newMemberDuty);
                });
    }

}
