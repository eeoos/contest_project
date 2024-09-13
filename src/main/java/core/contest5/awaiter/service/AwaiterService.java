package core.contest5.awaiter.service;

import core.contest5.global.exception.ResourceNotFoundException;
import core.contest5.global.exception.UnauthorizedException;
import core.contest5.member.domain.Member;
import core.contest5.member.repository.MemberJpaRepository;
import core.contest5.post.domain.Post;
import core.contest5.post.repository.PostJpaRepository;
import core.contest5.awaiter.domain.*;
import core.contest5.awaiter.repository.AwaiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwaiterService {

    private final AwaiterRepository awaiterRepository;
    private final PostJpaRepository postJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    public List<AwaiterResponseDto> getAwaiterList(Long postId) {

        return awaiterRepository.findByPostId(postId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AwaiterResponseDto getAwaiter(Long postId, Long awaiterId) {
        Awaiter awaiter = awaiterRepository.findByIdAndPostId(awaiterId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("Awaiter not found"));
        return convertToDto(awaiter);
    }

    public AwaiterResponseDto applyAsAwaiter(Long postId, AwaiterRequestDto awaiterRequestDto) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        Member member = memberJpaRepository.findById(awaiterRequestDto.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        Awaiter newAwaiter = Awaiter.builder()
                .post(post)
                .member(member)
                .profileDescription(awaiterRequestDto.getProfileDescription())
                .registrationDate(LocalDateTime.now())
                .applicationStatus(ApplicationStatus.PENDING)
                .build();

        Awaiter savedAwaiter = awaiterRepository.save(newAwaiter);
        return convertToDto(savedAwaiter);
    }

    public AwaiterResponseDto updateAwaiterStatus(Long postId, Long awaiterId, AwaiterStatusUpdateDto statusUpdateDto) {
        Awaiter awaiter = awaiterRepository.findByIdAndPostId(awaiterId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("Awaiter not found"));

        awaiter.updateApplicationStatus(statusUpdateDto.getNewStatus());
        Awaiter updatedAwaiter = awaiterRepository.save(awaiter);
        return convertToDto(updatedAwaiter);
    }

    private AwaiterResponseDto convertToDto(Awaiter awaiter) {
        return AwaiterResponseDto.builder()
                .id(awaiter.getId())
                .postId(awaiter.getPost().getId())
                .memberId(awaiter.getMember().getId())
                .memberName(awaiter.getMember().getName())
                .profileDescription(awaiter.getProfileDescription())
                .registrationDate(awaiter.getRegistrationDate())
                .applicationStatus(awaiter.getApplicationStatus())
                .build();
    }

    public void deleteAwaiter(Long postId, Long awaiterId, Long requesterId) {
        Awaiter awaiter = awaiterRepository.findByIdAndPostId(awaiterId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("Awaiter not found"));

        //본인 확인
        if (!awaiter.getMember().getId().equals(requesterId)) {

            throw new UnauthorizedException("본인이 아니면 삭제할 수 없습니다");
        }

        awaiterRepository.delete(awaiter);
    }


}