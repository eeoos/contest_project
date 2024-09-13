package core.contest5.awaiter.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AwaiterRequestDto {
    private Long memberId;
    private String profileDescription;
}
