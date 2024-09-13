package core.contest5.team.api;

import core.contest5.member.domain.MemberResponse;
import core.contest5.team.domain.TeamCreateRequestDto;
import core.contest5.team.domain.TeamResponseDto;
import core.contest5.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamResponseDto> createTeam(@RequestBody TeamCreateRequestDto requestDto) {
        TeamResponseDto responseDto = teamService.createTeam(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<MemberResponse>> getTeamMembers(@PathVariable Long teamId) {
        List<MemberResponse> members = teamService.getTeamMembers(teamId);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{teamId}/members/{memberId}")
    public ResponseEntity<MemberResponse> getTeamMemberProfile(@PathVariable Long teamId, @PathVariable Long memberId) {
        MemberResponse member = teamService.getTeamMemberProfile(teamId, memberId);
        return ResponseEntity.ok(member);
    }
}