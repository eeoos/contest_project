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
    private Long id;
    private Long postId;
    private Long teamId;
    private String teamName;
    private String teamLeaderName;
    private LocalDateTime registrationDate;
    private ApplicationStatus applicationStatus;
}