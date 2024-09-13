package core.contest5.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberReader {

    private final MemberRepository memberRepository;

    public MemberDomain read(Long userId) {

        return memberRepository.findById(userId);
    }

    public MemberDomain read(String email) {
        return memberRepository.findByEmail(email);
    }

//    public UserDomain readByName(String username) {
//        return userRepository.findByUsername(username);
//    }
}

