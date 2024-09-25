package core.contest5.team.api;

import core.contest5.member.domain.MemberResponse;
import core.contest5.team.domain.TeamResponseDto;
import core.contest5.team.domain.TeamWaiterDomain;
import core.contest5.team.domain.TeamWaiterRequestDto;
import core.contest5.team.domain.TeamWaiterResponseDto;
import core.contest5.team.service.TeamWaiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class TeamWaiterController {
    private final TeamWaiterService teamWaiterService;

    @PostMapping("/{postId}/teams")
    public ResponseEntity<TeamWaiterResponseDto> registerTeamWaiter(
            @PathVariable Long postId,
            @RequestBody TeamWaiterRequestDto requestDto) {
        TeamWaiterDomain teamWaiterDomain = teamWaiterService.registerTeamWaiter(postId, requestDto.getTeamId());
        return ResponseEntity.status(HttpStatus.CREATED).body(TeamWaiterResponseDto.from(teamWaiterDomain));
    }

    @GetMapping("/{postId}/teams")
    public ResponseEntity<List<TeamWaiterResponseDto>> getTeamWaiterList(@PathVariable Long postId) {
        List<TeamWaiterDomain> teamWaiterList = teamWaiterService.getTeamWaiterList(postId);
        return ResponseEntity.ok(teamWaiterList.stream().map(TeamWaiterResponseDto::from).toList());
    }

    @GetMapping("/{postId}/teams/{teamId}")
    public ResponseEntity<TeamResponseDto> getTeamProfile(@PathVariable Long postId, @PathVariable Long teamId) {
        TeamWaiterDomain teamProfile = teamWaiterService.getTeamProfile(postId, teamId);
        return ResponseEntity.ok(TeamResponseDto.from(teamProfile.getTeam()));
    } // TeamController의 getTeam과 같음
    // 신청은 TeamController에 구현해야하는가? TeamWaiterController에 구현해야하는가? - 질문



    @DeleteMapping("/{postId}/teams/{teamId}")
    public ResponseEntity<Void> deleteTeamWaiter(
            @PathVariable Long postId,
            @PathVariable Long teamId) {

        teamWaiterService.deleteTeamWaiter(postId, teamId);
        return ResponseEntity.noContent().build();
    }
}