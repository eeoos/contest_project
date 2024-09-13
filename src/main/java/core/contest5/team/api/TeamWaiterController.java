package core.contest5.team.api;

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

    @PostMapping("/{postId}/teamWaiterList")
    public ResponseEntity<TeamWaiterResponseDto> registerTeamWaiter(
            @PathVariable Long postId,
            @RequestBody TeamWaiterRequestDto requestDto) {
        TeamWaiterResponseDto responseDto = teamWaiterService.registerTeamWaiter(postId, requestDto.getTeamId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{postId}/teamWaiterList")
    public ResponseEntity<List<TeamWaiterResponseDto>> getTeamWaiterList(@PathVariable Long postId) {
        List<TeamWaiterResponseDto> teamWaiterList = teamWaiterService.getTeamWaiterList(postId);
        return ResponseEntity.ok(teamWaiterList);
    }

    @DeleteMapping("/{postId}/teamWaiterList/{teamWaiterId}")
    public ResponseEntity<Void> deleteTeamWaiter(
            @PathVariable Long postId,
            @PathVariable Long teamWaiterId) {
        teamWaiterService.deleteTeamWaiter(postId, teamWaiterId);
        return ResponseEntity.noContent().build();
    }
}