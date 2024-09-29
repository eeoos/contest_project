package core.contest5.admin.api;

import core.contest5.admin.domain.SanctionRequest;
import core.contest5.admin.domain.SanctionMemberResponse;
import core.contest5.admin.service.SanctionService;
import core.contest5.member.service.MemberDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/home")
@RequiredArgsConstructor
public class AdminController {

    private final SanctionService sanctionService;

    @GetMapping
    public ResponseEntity<Long> getDailyNewSignups() {
        return ResponseEntity.ok(sanctionService.getDailyNewSignups());
    }

    /*@PostMapping("/report")
    public ResponseEntity<List<>> getReportingAction() {

    }*/

    @GetMapping("/sanctions/{memberId}")
    public ResponseEntity<SanctionMemberResponse> getUser(@PathVariable Long memberId) {
        MemberDomain memberDomain = sanctionService.getMemberById(memberId);
        return ResponseEntity.ok(SanctionMemberResponse.from(memberDomain));
    }
    @PostMapping("/sanctions")
    public ResponseEntity<Void> applyReportAction(@RequestBody SanctionRequest request) {
        sanctionService.applySanction(request.getMemberId(), request.getSanctionType());
        return ResponseEntity.ok().build();
    }
}
