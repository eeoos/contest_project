package core.contest5.team.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponseDto {
    private Long id;
    private String name;
    private Long leaderId;
    private String leaderName;
    private String description;
}
