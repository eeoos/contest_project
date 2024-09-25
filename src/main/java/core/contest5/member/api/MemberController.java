package core.contest5.member.api;

import core.contest5.file.FileService;
import core.contest5.member.domain.MemberResponse;
import core.contest5.member.domain.UpdateMemberRequest;
import core.contest5.member.service.MemberDomain;
import core.contest5.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final FileService fileService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMemberInfo(@PathVariable Long memberId) {
        MemberDomain member = memberService.readMember(memberId);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @PutMapping(value = "{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateMemberInfo(
            @PathVariable Long memberId,
            @RequestPart("memberInfo") UpdateMemberRequest updateMemberRequest,
            @RequestPart(value = "thumbnailImage", required = false) MultipartFile profileImage,
            @RequestPart(value = "contestEntries", required = false) List<MultipartFile> contestEntries) throws IOException {
        String profileImageName = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            profileImageName = fileService.saveFile(profileImage, "member");
        }

        List<String> contestEntryNames = new ArrayList<>();
        if (contestEntries != null && !contestEntries.isEmpty()) {
            contestEntryNames = fileService.saveFiles(contestEntries, "member");
        }

        memberService.updateMember(memberId, updateMemberRequest, profileImageName, contestEntryNames);
        return ResponseEntity.ok().build();
    }
}
