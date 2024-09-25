package core.contest5.awaiter.service;

import core.contest5.awaiter.domain.*;
import core.contest5.member.service.MemberDomain;
import core.contest5.member.service.MemberReader;
import core.contest5.post.service.PostDomain;
import core.contest5.post.service.PostReader;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class TeamApplicationService {
    private final TeamApplicationRepository teamApplicationRepository;
    private final MemberReader memberReader;
    private final PostReader postReader;
    private final AwaiterRepository awaiterRepository;

    @Transactional
    public void applyForTeam(Long postId, Long awaiterId, Long applicantId) {

        AwaiterId postIdAndAwaiterId = new AwaiterId(awaiterId, postId);

        AwaiterDomain awaiterDomain = awaiterRepository.findById(postIdAndAwaiterId);
        if (awaiterDomain == null) {
            throw new EntityNotFoundException("Awaiter not found");
        }

        PostDomain postDomain = postReader.read(postId);
        if (postDomain == null) {
            throw new EntityNotFoundException("Post not found");
        }

        MemberDomain awaiterMemberDomain = memberReader.read(awaiterId);
        MemberDomain applicantMemberDomain = memberReader.read(applicantId);
        if (awaiterMemberDomain == null || applicantMemberDomain == null) {
            throw new EntityNotFoundException("Member not found");
        }

        // Check if the awaiter is actually in the awaiter list for this post
        if (!awaiterRepository.existsById(postIdAndAwaiterId)) {
            throw new IllegalStateException("포스트 대기 목록에서 유저를 찾을 수 없습니다.");
        }
        TeamApplicationId teamApplicationId = new TeamApplicationId(postDomain.getId(), applicantMemberDomain.getId(), awaiterMemberDomain.getId());

        // Check if an application already exists

        if (teamApplicationRepository.existsById(teamApplicationId)) {
            throw new IllegalStateException("신청이 이미 존재합니다.");
        }

        // Application doesn't exist, so we can create a new one
        TeamApplicationDomain newApplication = new TeamApplicationDomain(
                teamApplicationId,
                postDomain,
                applicantMemberDomain,
                awaiterMemberDomain,
                LocalDateTime.now(),
                ApplicationStatus.PENDING
        );

        teamApplicationRepository.save(newApplication);
    }
}

