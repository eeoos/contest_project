package core.contest5.team.api;

import core.contest5.member.domain.MemberResponse;
import core.contest5.team.domain.TeamCreateRequestDto;
import core.contest5.team.domain.TeamDomain;
import core.contest5.team.domain.TeamMemberDomain;
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
        TeamDomain team = teamService.createTeam(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(TeamResponseDto.from(team));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponseDto> getTeam(@PathVariable Long teamId) {
        TeamDomain team = teamService.getTeam(teamId);
        return ResponseEntity.ok(TeamResponseDto.from(team));
    }

    @GetMapping("/{teamId}/teamMembers/{teamMemberId}")
    public ResponseEntity<MemberResponse> getTeamMemberProfile(@PathVariable Long teamId, @PathVariable Long teamMemberId) {
        TeamMemberDomain teamMemberProfile = teamService.getTeamMemberProfile(teamId, teamMemberId);
        return ResponseEntity.ok(MemberResponse.from(teamMemberProfile.getMember()));
    }
}