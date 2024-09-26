package core.contest5.post.domain;

import core.contest5.post.service.PostDomain;
import core.contest5.post.service.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContestStatusScheduler {

    private final PostRepository postRepository;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    @Transactional
    public void updateContestStatus() {
        List<PostDomain> activePosts = postRepository.findByContestStatusIn(List.of(ContestStatus.NOT_STARTED, ContestStatus.IN_PROGRESS));
        List<PostDomain> updatedPosts = activePosts.stream()
                .filter(post -> {
                    ContestStatus oldStatus = post.getContestStatus();
                    post.updateStatus();
                    return oldStatus != post.getContestStatus();
                })
                .collect(Collectors.toList());

        if (!updatedPosts.isEmpty()) {
            postRepository.saveAll(updatedPosts);
        }
    }
}