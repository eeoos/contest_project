package core.contest5.awaiter.api;

import core.contest5.awaiter.domain.AwaiterRequestDto;
import core.contest5.awaiter.domain.AwaiterResponseDto;
import core.contest5.awaiter.domain.AwaiterStatusUpdateDto;
import core.contest5.awaiter.domain.TeamApplication;
import core.contest5.awaiter.service.AwaiterService;
import core.contest5.awaiter.service.TeamApplicationService;
import core.contest5.member.domain.Member;
import core.contest5.member.service.MemberDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class AwaiterController {

    private final AwaiterService awaiterService;
    private final TeamApplicationService teamApplicationService;


    //대기자 목록
    @GetMapping("/{postId}/awaiterList")
    public ResponseEntity<List<AwaiterResponseDto>> getAwaiterList(@PathVariable Long postId) {
        List<AwaiterResponseDto> awaiterList = awaiterService.getAwaiterList(postId);
        return ResponseEntity.ok(awaiterList);
    }

    //특정 대기자 프로필
    @GetMapping("/{postId}/awaiterList/{awaiterId}")
    public ResponseEntity<AwaiterResponseDto> getAwaiter(@PathVariable Long postId, @PathVariable Long awaiterId) {
        AwaiterResponseDto awaiter = awaiterService.getAwaiter(postId, awaiterId);
        return ResponseEntity.ok(awaiter);
    }

    //대기자 목록에 등록
    @PostMapping("/{postId}/awaiterList")
    public ResponseEntity<AwaiterResponseDto> applyAsAwaiter(
            @PathVariable Long postId,
            @RequestBody AwaiterRequestDto awaiterRequestDto) {
        AwaiterResponseDto newAwaiter = awaiterService.applyAsAwaiter(postId, awaiterRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAwaiter);
    }

    //상태 수정
    @PutMapping("/{postId}/awaiterList/{awaiterId}")
    public ResponseEntity<AwaiterResponseDto> updateAwaiterStatus(
            @PathVariable Long postId,
            @PathVariable Long awaiterId,
            @RequestBody AwaiterStatusUpdateDto statusUpdateDto) {
        AwaiterResponseDto updatedAwaiter = awaiterService.updateAwaiterStatus(postId, awaiterId, statusUpdateDto);
        return ResponseEntity.ok(updatedAwaiter);
    }

    //팀원 신청
    @PostMapping("/{postId}/awaiterList/{awaiterId}/apply")
    public ResponseEntity<TeamApplication> applyForTeam(
            @PathVariable Long postId,
            @PathVariable Long awaiterId,
            @AuthenticationPrincipal MemberDomain memberDomain) {
        Long applicantId = memberDomain.id();
        TeamApplication application = teamApplicationService.applyForTeam(postId, awaiterId, applicantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }

    // 대기자 목록에서 자신을 삭제
    @DeleteMapping("/{postId}/awaiterList/{awaiterId}")
    public ResponseEntity<Void> deleteAwaiter(
            @PathVariable Long postId,
            @PathVariable Long awaiterId,
            @AuthenticationPrincipal MemberDomain memberDomain) {
        Long requesterId = memberDomain.id();
        awaiterService.deleteAwaiter(postId, awaiterId, requesterId);
        return ResponseEntity.noContent().build();
    }
}