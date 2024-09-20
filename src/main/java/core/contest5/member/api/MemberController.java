package core.contest5.member.api;

import core.contest5.member.domain.MemberResponse;
import core.contest5.member.domain.UpdateMemberRequest;
import core.contest5.member.service.MemberDomain;
import core.contest5.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMemberInfo(@PathVariable Long memberId) {
        MemberDomain member = memberService.readMember(memberId);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @PutMapping("{memberId}")
    public ResponseEntity<Void> updateMemberInfo(@PathVariable Long memberId, @RequestBody UpdateMemberRequest updateMemberRequest) {

        memberService.updateMember(memberId, updateMemberRequest);
        return ResponseEntity.ok().build();
    }
}
