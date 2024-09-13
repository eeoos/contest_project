package core.contest5.member.api;

import core.contest5.global.jwt.JwtDto;
import core.contest5.member.service.MemberService;
import core.contest5.member.domain.SignUpRequest;
import core.contest5.member.social.SocialLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final MemberService memberService;
    private final SocialLoginService socialLoginService;

    @GetMapping("/oauth2/{provider}")
    public void tryOAuth2(@PathVariable String provider, HttpServletResponse response)
        throws IOException {
        String url = socialLoginService.tryOAuth2(provider);
        response.sendRedirect(url);
    }

    @GetMapping("/oauth2/code/{provider}")
    public ResponseEntity<JwtDto> authorized(@PathVariable String provider, @RequestParam String code) {
        return socialLoginService.connectToSocialSignIn(provider, code);
    }

    @PostMapping("/social/{provider}")
    public ResponseEntity<JwtDto> socialSignIn(@PathVariable String provider, @RequestBody String code) {
        SignUpRequest signUpRequest = socialLoginService.signIn(provider, code);
        return ResponseEntity.ok(memberService.socialSignIn(signUpRequest.toMemberInfo()));
    }

    /*@PostMapping("/local/signup")
    public ResponseEntity<Member> signUp(@RequestBody SignUpForm form){
        return ResponseEntity.ok(memberService.signUp(form));
    }

    @PostMapping("/local/signin")
    public ResponseEntity<JwtDto> signIn(@RequestBody SignUpForm form){
        return ResponseEntity.ok(memberService.signIn(form));
    }*/

}
