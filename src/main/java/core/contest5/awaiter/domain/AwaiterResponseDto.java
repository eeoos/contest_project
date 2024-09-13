package core.contest5.awaiter.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AwaiterResponseDto {
    private Long id;
    private Long postId;
    private Long memberId;
    private String memberName;
    private String profileDescription;
    private LocalDateTime registrationDate;
    private ApplicationStatus applicationStatus;
}