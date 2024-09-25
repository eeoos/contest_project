package core.contest5.team.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class TeamWaiterId implements Serializable {
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "team_id")
    private Long teamId;
}