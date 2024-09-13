package core.contest5.member.api;

import core.contest5.member.domain.Member;
import core.contest5.member.service.MemberDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/info")
    public ResponseEntity<MemberDomain> info(@AuthenticationPrincipal MemberDomain member){
        return ResponseEntity.ok(member);
    }
}
