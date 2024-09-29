package core.contest5.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SanctionRequest {
    private Long memberId;
    private SanctionType sanctionType;
}
