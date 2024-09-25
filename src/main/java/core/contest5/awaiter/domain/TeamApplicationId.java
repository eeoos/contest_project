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
public class TeamApplicationId implements Serializable {

    @Column(name ="post_id")
    private Long postId;

    @Column(name ="applicant_id")
    private Long applicantId;

    @Column(name ="awaiter_id")
    private Long awaiterId;


}
