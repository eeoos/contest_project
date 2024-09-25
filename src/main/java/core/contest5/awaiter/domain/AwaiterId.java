package core.contest5.awaiter.domain;

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
public class AwaiterId implements Serializable {
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "post_id")
    private Long postId;
}
