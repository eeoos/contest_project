package core.contest5.team.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamCreateRequestDto {
    private String name;
    private Long leaderId;
    private String description;
}
