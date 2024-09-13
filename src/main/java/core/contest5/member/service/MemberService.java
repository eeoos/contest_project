package core.contest5.member.service;

import core.contest5.global.jwt.JwtDto;
import core.contest5.global.jwt.JwtIssuer;
import core.contest5.member.domain.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtIssuer jwtIssuer;

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

        return jwtIssuer.createToken(domain.info().email(), domain.info().memberRole().name());
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
}
