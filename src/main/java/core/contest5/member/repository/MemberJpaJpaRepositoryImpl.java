package core.contest5.member.repository;

import core.contest5.admin.domain.SuspensionStatus;
import core.contest5.member.domain.*;
import core.contest5.member.domain.memberinfo.MemberRole;
import core.contest5.member.service.MemberDomain;
import core.contest5.member.service.MemberInfo;
import core.contest5.member.service.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

@Repository
@RequiredArgsConstructor
public class MemberJpaJpaRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Long save(MemberInfo memberInfo) {
        Member member = memberJpaRepository.save(Member.builder()
                .email(memberInfo.email())
                .name(memberInfo.name())
                .memberRole(MemberRole.ADMIN)
                .profileImage(memberInfo.profileImage())
                .memberField(memberInfo.memberField())
                .memberDuty(memberInfo.memberDuty())
                .school(memberInfo.school())
                .grade(memberInfo.grade())
                .major(memberInfo.major())
                .techStacks(Collections.emptySet())
                .certificates(Collections.emptySet())
                .awardFileNames(Collections.emptySet())
                .contestEntries(Collections.emptySet())
                .warningCount(0)
                .suspensionStatus(SuspensionStatus.ACTIVE)
                //suspensionEndTime 넣지 않아도 되는지?
                .build());

        return memberJpaRepository.save(member).getId();
    }

    @Override
    public MemberDomain findByEmail(String email) throws EntityNotFoundException {
        return memberJpaRepository.findByEmail(email)
                .map(Member::toDomain)
                .orElseThrow(
                        () -> new EntityNotFoundException("User not found"));
    }

    @Override
    public MemberDomain findById(Long userId) {
        return memberJpaRepository.findById(userId)
                .map(Member::toDomain)
                .orElseThrow(
                        () -> new EntityNotFoundException("User not found"));
    }

    @Override
    public void update(MemberDomain domain) {
        Member existingMember = memberJpaRepository.findById(domain.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Member updatedMember = Member.builder()
                .id(existingMember.getId())  // ID는 변경되지 않음
                .email(existingMember.getEmail())  // 이메일도 변경되지 않음
                .name(domain.getMemberInfo().name())
                .profileImage(domain.getMemberInfo().profileImage())
                .memberField(domain.getMemberInfo().memberField())
                .memberDuty(domain.getMemberInfo().memberDuty())
                .grade(domain.getMemberInfo().grade())
                .school(domain.getMemberInfo().school())
                .major(domain.getMemberInfo().major())
                .memberRole(existingMember.getMemberRole())
                .techStacks(domain.getMemberInfo().techStacks())
                .certificates(domain.getMemberInfo().certificates())
                .awardFileNames(domain.getMemberInfo().awards())
                .contestEntries(domain.getMemberInfo().contestEntries())
                .warningCount(domain.getWarningCount())
                .suspensionStatus(domain.getStatus())
                .suspensionEndTime(domain.getSuspensionEndTime())
                .createdAt(existingMember.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();


        memberJpaRepository.save(updatedMember);
    }

    @Override
    public Long countMembersJoinedToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        return memberJpaRepository.countMembersJoinedToday(startOfDay);
    }
}
