
package core.contest5.post.service;

import core.contest5.awaiter.domain.Awaiter;
import core.contest5.member.service.MemberDomain;
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




    public void update(UpdatedPostInfo updatedPostInfo) {
        this.postInfo = updatedPostInfo.postInfo();
    }

    public void increaseViewCount() {
        viewCount++;
    }

    public void increaseBookmarkCount() {
        bookmarkCount++;
    }
}
