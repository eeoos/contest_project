package core.contest5.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.contest5.awaiter.domain.Awaiter;
import core.contest5.member.domain.Member;
import core.contest5.post.service.PostDomain;
import core.contest5.post.service.PostInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Builder.Default
    @Column(name = "view_count")
    private Long viewCount=0L;

    @Builder.Default
    @Column(name = "bookmark_count")
    private Long bookmarkCount=0L;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(name = "poster_image")
    private String posterImage;

    @Column(name = "qualification")
    //응모 자격
    private String qualification;

    //시상 규모
    @Column(name = "award_scale")
    private String awardScale;

    //주최 기관
    @Column(name = "host")
    private String host;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member writer;

    @Column(name = "host_homepage_url")
    private String hostHomepageURL;

    @ElementCollection(targetClass = PostField.class)
    @CollectionTable(name = "post_fields", joinColumns = @JoinColumn(name = "post_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<PostField> postFields = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Awaiter> awaiters = new ArrayList<>(); // 대기자 목록

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContestStatus contestStatus;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    public static Post from(PostDomain post) {
        return Post.builder()
                .id(post.getId())
                .title(post.getPostInfo().title())
                .content(post.getPostInfo().content())
                .viewCount(post.getViewCount())
                .bookmarkCount(post.getBookmarkCount())
                .startDate(post.getPostInfo().startDate())
                .endDate(post.getPostInfo().endDate())
                .posterImage(post.getPostInfo().posterImage())
                .qualification(post.getPostInfo().qualification())
                .awardScale(post.getPostInfo().awardScale())
                .host(post.getPostInfo().host())
                .hostHomepageURL(post.getPostInfo().hostHomepageURL())
                .postFields(post.getPostInfo().postFields())
//                .awaiters(post.getAwaiterList())
                .contestStatus(post.getPostInfo().contestStatus()) //추가
                .postFields(post.getPostInfo().postFields())
                .writer(Member.from(post.getMember()))
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static Post from(Long postId) {
        return Post.builder()
                .id(postId)
                .build();
    }

    public PostDomain toDomain() {
        return new PostDomain(
                id,
                new PostInfo(
                        title,
                        content,
                        startDate,
                        endDate,
                        posterImage,
                        qualification,
                        awardScale,
                        host,
                        hostHomepageURL,
                        postFields,
                        contestStatus
                ),
                viewCount,
                bookmarkCount,
//                awaiters,
                writer.toDomain(),
                startDate,
                endDate
        );
    }

    // 새로운 메서드: 엔티티 업데이트
    public void updateFrom(PostInfo postInfo) {
        this.title = postInfo.title();
        this.content = postInfo.content();
        this.startDate = postInfo.startDate();
        this.endDate = postInfo.endDate();
        this.posterImage = postInfo.posterImage();
        this.qualification = postInfo.qualification();
        this.awardScale = postInfo.awardScale();
        this.host = postInfo.host();
        this.hostHomepageURL = postInfo.hostHomepageURL();
        this.contestStatus = postInfo.contestStatus();
        updatePostFields(postInfo.postFields());
    }

    // 새로운 메서드: postFields 업데이트
    private void updatePostFields(Set<PostField> newPostFields) {
        this.postFields.clear();
        this.postFields.addAll(newPostFields);
    }

    public void updateAwaiters(List<Awaiter> newAwaiters) {
        this.awaiters.clear();
        if (newAwaiters != null) {
            newAwaiters.forEach(awaiter -> awaiter.changePost(this));
            this.awaiters.addAll(newAwaiters);
        }
    }

    // Awaiter 추가 메서드
    public void addAwaiter(Member member) {
        Awaiter awaiter = new Awaiter(this, member);
        this.awaiters.add(awaiter);
    }

    // Awaiter 제거 메서드
    public void removeAwaiter(Awaiter awaiter) {
        this.awaiters.remove(awaiter);
        awaiter.changePost(null);
    }
}
