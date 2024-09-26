package core.contest5.awaiter.service;

import core.contest5.global.exception.UnauthorizedException;
import core.contest5.member.service.MemberDomain;
import core.contest5.member.service.MemberReader;
import core.contest5.awaiter.domain.*;
import core.contest5.post.service.PostDomain;
import core.contest5.post.service.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AwaiterService {

    private final AwaiterRepository awaiterRepository;
    private final PostRepository postRepository;
    private final MemberReader memberReader;


    @Transactional
    public List<AwaiterDomain> getAwaiterList(Long postId) {
        return awaiterRepository.findByPostId(postId);

    }

    @Transactional
    public MemberDomain getAwaiter(Long postId, Long memberId) {
        AwaiterId id = new AwaiterId(memberId, postId);
        AwaiterDomain awaiterDomain = awaiterRepository.findById(id);

        return memberReader.read(awaiterDomain.getId().getMemberId());
    }

    @Transactional
    public void applyAsAwaiter(Long postId, AwaiterRequest request) {
        PostDomain postDomain = postRepository.findById(postId);
        MemberDomain memberDomain = memberReader.read(request.getMemberId());

        AwaiterDomain newAwaiterDomain = AwaiterDomain.builder()
                .id(new AwaiterId(memberDomain.getId(), postDomain.getId()))
                .post(postDomain)
                .member(memberDomain)
                .applicationStatus(ApplicationStatus.PENDING)
                .build();

        awaiterRepository.save(newAwaiterDomain);
        postRepository.incrementAwaiterCount(postId);
    }

    @Transactional
    public AwaiterDomain updateAwaiterStatus(Long postId, Long memberId, AwaiterStatusUpdateDto statusUpdateDto) {
        PostDomain postDomain = postRepository.findById(postId);
        MemberDomain memberDomain = memberReader.read(memberId);

        AwaiterId id = new AwaiterId(memberDomain.getId(), postDomain.getId());

        AwaiterDomain awaiterDomain = awaiterRepository.findById(id);
        awaiterDomain.updateApplicationStatus(statusUpdateDto.getNewStatus());
        awaiterRepository.save(awaiterDomain);

        AwaiterDomain savedAwaiterDomain = awaiterRepository.findById(id);
        return savedAwaiterDomain;
    }
    @Transactional
    public void deleteAwaiter(Long postId, Long memberId, Long requesterId) {

        PostDomain postDomain = postRepository.findById(postId);
        MemberDomain memberDomain = memberReader.read(memberId);

        AwaiterId id = new AwaiterId(memberDomain.getId(), postDomain.getId());

        AwaiterDomain awaiterDomain = awaiterRepository.findById(id);

        //본인 확인
        if (!awaiterDomain.getId().getMemberId().equals(requesterId)) {
            throw new UnauthorizedException("본인이 아니면 삭제할 수 없습니다");
        }

        awaiterRepository.delete(awaiterDomain);
        postRepository.decrementAwaiterCount(postId);
    }
}