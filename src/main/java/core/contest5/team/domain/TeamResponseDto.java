package core.contest5.team.domain;

import core.contest5.awaiter.domain.AwaiterResponseDto;
import core.contest5.member.domain.MemberResponse;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponseDto {
    private Long id;
    private String name;
    private String description;
    private List<TeamMemberResponse> teamMembers; //AwaiterResponseDto를 또 써도 되는지? -> postId때매 안됨, postId꼭 넘겨야하는지? 질문

    public static TeamResponseDto from(TeamDomain domain) {
        if (domain == null) {
            return null;
        }
        return new TeamResponseDto(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                Optional.ofNullable(domain.getTeamMembers())
                        .map(members -> members.stream()
                                .map(TeamMemberResponse::from)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList()))
                        .orElse(Collections.emptyList())
        );
    }
}

