package core.contest5.team.domain;

import core.contest5.awaiter.domain.ApplicationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamWaiterResponseDto {
    private Long teamId;
    private Long postId;
    private String teamName;
    private String description;
    private ApplicationStatus applicationStatus;

    public static TeamWaiterResponseDto from(TeamWaiterDomain domain) {
        return new TeamWaiterResponseDto(
                domain.getTeam().getId(),
                domain.getPost().getId(),
                domain.getTeam().getName(),
                domain.getTeam().getDescription(),
                domain.getApplicationStatus()
        );
    }
}