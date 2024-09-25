package core.contest5.member.service;

import core.contest5.global.jwt.JwtDto;
import core.contest5.global.jwt.JwtIssuer;
import core.contest5.member.domain.Member;
import core.contest5.member.domain.UpdateMemberRequest;
import core.contest5.member.domain.UpdatedMemberInfo;
import core.contest5.member.domain.memberinfo.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtIssuer jwtIssuer;
    private final MemberReader memberReader;
    private final MemberUpdator memberUpdator;
    private final MemberFieldService memberFieldService;
    private final MemberDutyService memberDutyService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return Member.from(memberRepository.findByEmail(email));
    }

    public JwtDto socialSignIn(MemberInfo memberInfo) {

        MemberDomain domain;
        try {
            domain = memberRepository.findByEmail(memberInfo.email());
        }catch (EntityNotFoundException e) {
            Long userId = memberRepository.save(memberInfo);
            domain = memberRepository.findById(userId);
        }

        return jwtIssuer.createToken(domain.getMemberInfo().email(), domain.getMemberInfo().memberRole().name());
    }

    @Transactional
    public void updateMember(Long memberId, UpdateMemberRequest updateMemberRequest, String thumbnailImage, List<String> contestEntryNames) {
        MemberField memberField = memberFieldService.findOrCreateMemberField(updateMemberRequest.memberField());
        MemberDuty memberDuty = memberDutyService.findOrCreateMemberDuty(updateMemberRequest.memberDuty());
        MemberDomain existingMember = memberReader.read(memberId);

        Set<TechStack> updatedTechStacks = updateOrCreateSet(
                existingMember.getMemberInfo().techStacks(),
                updateMemberRequest.techStacks(),
                TechStack::getName,
                (techStackName, member) -> new TechStack(techStackName, member), //TechStack::new?
                memberId
        );

        Set<Certificate> updatedCertificates = updateOrCreateSet(
                existingMember.getMemberInfo().certificates(),
                updateMemberRequest.certificates(),
                Certificate::getName,
                (certificateName, member) -> new Certificate(certificateName, member),
                memberId
        );

        Set<ContestEntry> updatedContestEntries = updateOrCreateContestEntries(
                existingMember.getMemberInfo().contestEntries(),
                contestEntryNames,
                memberId
        );

        Set<Award> updatedAwards = updateOrCreateSet(
                existingMember.getMemberInfo().awards(),
                updateMemberRequest.awards(),
                Award::getName,
                (awardName, member) -> new Award(awardName, member),
                memberId
        );

        UpdatedMemberInfo updatedInfo = new UpdatedMemberInfo(
                memberId,
                new MemberInfo(
                        existingMember.getMemberInfo().email(),
                        updateMemberRequest.name(),
                        thumbnailImage,
                        existingMember.getMemberInfo().memberRole(),
                        memberField,
                        memberDuty,
                        updateMemberRequest.grade(),
                        updateMemberRequest.school(),
                        updateMemberRequest.major(),
                        updatedTechStacks,
                        updatedCertificates,
                        updatedContestEntries,
                        updatedAwards
                )
        );
        memberUpdator.update(existingMember, updatedInfo);
    }

    private <T> Set<T> updateOrCreateSet(
            Set<T> existingItems,
            Collection<String> newNames,
            Function<T, String> nameGetter,
            BiFunction<String, Member, T> constructor,
            Long memberId
    ) {
        Set<T> resultSet = new HashSet<>(existingItems);
        Member member = Member.from(memberId);

        // Remove items that are no longer in the new set
        resultSet.removeIf(item -> !newNames.contains(nameGetter.apply(item)));

        // Add new items
        for (String name : newNames) {
            boolean exists = resultSet.stream()
                    .anyMatch(item -> nameGetter.apply(item).equals(name));
            if (!exists) {
                resultSet.add(constructor.apply(name, member));
            }
        }

        return resultSet;
    }

    private Set<ContestEntry> updateOrCreateContestEntries(
            Set<ContestEntry> existingEntries,
            List<String> newFileNames,
            Long memberId
    ) {
        Set<ContestEntry> resultSet = new HashSet<>(existingEntries);
        Member member = Member.from(memberId);

        // Remove entries that are no longer in the new set
        resultSet.removeIf(entry -> !newFileNames.contains(entry.getName()));

        // Add new entries
        for (String fileName : newFileNames) {
            boolean exists = resultSet.stream()
                    .anyMatch(entry -> entry.getName().equals(fileName));
            if (!exists) {
                resultSet.add(new ContestEntry(fileName, member));
            }
        }

        return resultSet;
    }
    public MemberDomain readMember(Long memberId) {
        MemberDomain member = memberReader.read(memberId);
        if (member.getMemberInfo().memberField() == null // 첫 로그인
                && member.getMemberInfo().memberDuty() == null
                && member.getMemberInfo().techStacks().isEmpty()
                && member.getMemberInfo().certificates().isEmpty()
                && member.getMemberInfo().contestEntries().isEmpty()
                && member.getMemberInfo().awards().isEmpty()) {
            member = new MemberDomain(member.getId(),
                    new MemberInfo(
                            member.getMemberInfo().email(),
                            member.getMemberInfo().name(),
                            member.getMemberInfo().profileImage(),
                            member.getMemberInfo().memberRole(),
                            member.getMemberInfo().memberField(), //new MemberField("Default Field"),
                            member.getMemberInfo().memberDuty(), //new MemberDuty("Default Field"),
                            member.getMemberInfo().grade(),
                            member.getMemberInfo().school(),
                            member.getMemberInfo().major(),
                            member.getMemberInfo().techStacks(),
                            member.getMemberInfo().certificates(),
                            member.getMemberInfo().contestEntries(),
                            member.getMemberInfo().awards()
                    ),
                    member.getCreatedAt(),
                    member.getUpdatedAt()
            );
        }
        return member;
    }

    public MemberDomain getMemberById(Long userId) {
        return memberRepository.findById(userId);
    }
        /*public Member signUp(SignUpForm form) {
        if(memberRepository.existsByEmail(form.getEmail())){
            throw new RuntimeException("사용중인 이메일입니다.");
        }
        return memberRepository.save(Member.builder()
            .email(form.getEmail())
            .name(form.getName())
            .memberRole(MemberRole.USER)
            .build());
    }

    public JwtDto signIn(SignUpForm form) {
        Member member = getMemberByEmail(form.getEmail());

        return jwtIssuer.createToken(member.getEmail(), member.getMemberRole().name());
    }*/

   /* public JwtDto socialSignIn(SignUpRequest form) {
        Member member;
        try {
            member = getMemberByEmail(form.getEmail());
        }catch (UsernameNotFoundException e) {
            member = memberRepository.save(Member.builder()
                    .email(form.getEmail())
                    .name(form.getName())
                    .memberRole(MemberRole.USER)
                    .profileImage(form.getProfileImage())
                    .build());
        }

        return jwtIssuer.createToken(member.getEmail(), member.getMemberRole().name());
    }*/

/*
    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("일치하는 정보가 없습니다."));
    }*/

    /*@Transactional
    public void updateMemberInfo(Long userId, UpdateMemberInfoRequest newInfo) {

        memberRepository.update(userId, newInfo);
    }*/
}
