package core.contest5.awaiter.service;

import core.contest5.awaiter.domain.ApplicationStatus;
import core.contest5.awaiter.domain.Awaiter;
import core.contest5.awaiter.domain.TeamApplication;
import core.contest5.awaiter.repository.AwaiterRepository;
import core.contest5.awaiter.repository.TeamApplicationRepository;
import core.contest5.global.exception.ResourceNotFoundException;
import core.contest5.member.domain.Member;
import core.contest5.member.repository.MemberJpaRepository;
import core.contest5.member.service.MemberRepository;
import core.contest5.post.domain.Post;
import core.contest5.post.repository.PostJpaRepository;
import core.contest5.post.service.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamApplicationService {
    private final TeamApplicationRepository teamApplicationRepository;
    private final PostRepository postRepository;
    private final PostJpaRepository postJpaRepository;
    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final AwaiterRepository awaiterRepository;

    @Transactional
    public TeamApplication applyForTeam(Long postId, Long awaiterId, Long applicantId) {

        Optional<Awaiter> optionalAwaiter = awaiterRepository.findById(awaiterId);
        Awaiter awaiter = optionalAwaiter.get(); //??

        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Member awaiterMember = memberJpaRepository.findById(awaiter.getMember().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Awaiter not found"));

        Member applicant = memberJpaRepository.findById(applicantId)
                .orElseThrow(() -> new ResourceNotFoundException("Applicant not found"));

        // Check if the awaiter is actually in the awaiter list for this post
        if (!awaiterRepository.existsByPostAndMember(post, awaiterMember)) {
            throw new IllegalStateException("포스트 대기 목록에서 유저를 찾을 수 없습니다.");
        }

        // Check if an application already exists
        Optional<TeamApplication> existingApplication = teamApplicationRepository
                .findByPostAndApplicantAndAwaiter(post, applicant, awaiterMember);

        if (existingApplication.isPresent()) {
            throw new IllegalStateException("Application already exists");
        }

        TeamApplication application = TeamApplication.builder()
                .post(post)
                .applicant(applicant)
                .awaiter(awaiterMember)
                .applicationDate(LocalDateTime.now())
                .status(ApplicationStatus.PENDING)
                .build();

        return teamApplicationRepository.save(application);
    }
}