
package core.contest5.post.service;

import core.contest5.awaiter.domain.Awaiter;
import core.contest5.member.service.MemberDomain;
import core.contest5.post.domain.ContestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostDomain {

    private Long id;
    private PostInfo postInfo;
    private Long viewCount;
    private Long bookmarkCount;
//    private List<Awaiter> awaiterList;
    private MemberDomain member;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long awaiterCount;
//    private ContestStatus contestStatus;



    public void update(UpdatedPostInfo updatedPostInfo) {
        this.postInfo = updatedPostInfo.postInfo();
    }

    public void increaseViewCount() {
        viewCount++;
    }

    public void increaseBookmarkCount() {
        bookmarkCount++;
    }

    public void decreaseBookmarkCount() {
        if (this.bookmarkCount > 0) {
            bookmarkCount--;
        }
    }

    public void updateStatus() {
        LocalDateTime now = LocalDateTime.now();
        ContestStatus newStatus;
        if (now.isBefore(postInfo.startDate())) {
            newStatus = ContestStatus.NOT_STARTED;
        } else if (now.isAfter(postInfo.endDate())) {
            newStatus = ContestStatus.CLOSED;
        } else {
            newStatus = ContestStatus.IN_PROGRESS;
        }

        // PostInfo가 불변(immutable)이라면, 새 PostInfo 인스턴스를 생성해야 합니다.
        this.postInfo = new PostInfo(
                postInfo.title(),
                postInfo.content(),
                postInfo.startDate(),
                postInfo.endDate(),
                postInfo.posterImage(),
                postInfo.attachedFiles(),
                postInfo.qualification(),
                postInfo.awardScale(),
                postInfo.host(),
                postInfo.hostHomepageURL(),
                postInfo.postFields(),
                newStatus  // 여기서 새로운 상태를 설정합니다.
        );
    }

    public ContestStatus getContestStatus() {
        return postInfo.contestStatus();
    }
}
