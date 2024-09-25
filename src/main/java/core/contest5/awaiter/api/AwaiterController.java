package core.contest5.awaiter.api;

import core.contest5.awaiter.domain.*;
import core.contest5.awaiter.service.AwaiterService;
import core.contest5.awaiter.service.TeamApplicationService;
import core.contest5.member.domain.MemberResponse;
import core.contest5.member.service.MemberDomain;
import core.contest5.member.service.MemberReader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        List<AwaiterDomain> awaiters = awaiterService.getAwaiterList(postId);

        return ResponseEntity.ok(awaiters.stream().map(AwaiterResponseDto::from).toList());
    }

    //특정 대기자 프로필
    @GetMapping("/{postId}/awaiterList/{awaiterId}")
    public ResponseEntity<MemberResponse> getAwaiter(@PathVariable Long postId, @PathVariable Long awaiterId) {
        MemberDomain awaiter = awaiterService.getAwaiter(postId, awaiterId);
        return ResponseEntity.ok(MemberResponse.from(awaiter));
    }

    //대기자 목록에 등록
    @PostMapping("/{postId}/awaiterList")
    public ResponseEntity<Void> applyAsAwaiter(
            @PathVariable Long postId,
            @RequestBody AwaiterRequest request
            ) {
        awaiterService.applyAsAwaiter(postId, request);
        return ResponseEntity.ok().build();
    }

    // 대기자 목록에서 자신을 삭제
    // details에서 지우는 것으로 가정
    // 팀 관리할 때 리팩토링
    @DeleteMapping("/{postId}/awaiterList/{awaiterId}")
    public ResponseEntity<Void> deleteAwaiter(
            @PathVariable Long postId,
            @PathVariable Long awaiterId,
            @AuthenticationPrincipal MemberDomain memberDomain) {
        Long requesterId = memberDomain.getId();
        awaiterService.deleteAwaiter(postId, awaiterId, requesterId);
        return ResponseEntity.noContent().build();
    }

    //상태 수정
    @PutMapping("/{postId}/awaiters/{awaiterId}")
    public ResponseEntity<AwaiterResponseDto> updateAwaiterStatus(
            @PathVariable Long postId,
            @PathVariable Long awaiterId,
            @Valid @RequestBody AwaiterStatusUpdateDto statusUpdateDto) {
        AwaiterDomain awaiterDomain = awaiterService.updateAwaiterStatus(postId, awaiterId, statusUpdateDto);
        return ResponseEntity.ok(AwaiterResponseDto.from(awaiterDomain));
    }

    //팀원 신청
    @PostMapping("/{postId}/awaiterList/{awaiterId}/apply")
    public ResponseEntity<Void> applyForTeam(
            @PathVariable Long postId,
            @PathVariable Long awaiterId,
            @AuthenticationPrincipal MemberDomain memberDomain) {
        Long applicantId = memberDomain.getId();
        teamApplicationService.applyForTeam(postId, awaiterId, applicantId);
        return ResponseEntity.ok().build();
    }
}