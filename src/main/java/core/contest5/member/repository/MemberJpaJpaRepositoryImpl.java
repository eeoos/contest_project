package core.contest5.member.repository;

import core.contest5.member.domain.Member;
import core.contest5.member.domain.MemberRole;
import core.contest5.member.service.MemberDomain;
import core.contest5.member.service.MemberInfo;
import core.contest5.member.service.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
