package core.contest5.member.api;

import core.contest5.file.FileService;
import core.contest5.member.domain.MemberResponse;
import core.contest5.member.domain.UpdateMemberRequest;
import core.contest5.member.service.MemberDomain;
import core.contest5.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
            @RequestPart(value = "awards", required = false) List<MultipartFile> awards) throws IOException {

        MemberDomain existingMember = memberService.getMemberById(memberId);
        String profileImageName = existingMember.getMemberInfo().profileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            profileImageName = fileService.saveFile(profileImage, "member");
        }

        Set<String> existingAwards = existingMember.getMemberInfo().awards();
        List<String> newAwardNames = new ArrayList<>(existingAwards);
        if (awards != null && !awards.isEmpty()) {
            List<String> uploadedAwards = fileService.saveFiles(awards, "award");
            newAwardNames.addAll(uploadedAwards);
        }

        memberService.updateMember(memberId, updateMemberRequest, profileImageName, newAwardNames);
        return ResponseEntity.ok().build();
    }
}
