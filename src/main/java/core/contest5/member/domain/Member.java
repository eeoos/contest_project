package core.contest5.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.contest5.admin.domain.SuspensionStatus;
import core.contest5.member.domain.memberinfo.*;
import core.contest5.member.service.MemberDomain;
import core.contest5.member.service.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String profileImage;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole memberRole;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Collections.singleton(new SimpleGrantedAuthority(this.memberRole.getAuthority()));
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @OneToOne
    @JoinColumn(name = "member_field_id")
    private MemberField memberField; // 관심 분야

    @OneToOne
    @JoinColumn(name = "member_duty_id")
    private MemberDuty memberDuty; // 역할(직무)

    @Enumerated(EnumType.STRING)
    private Grade grade; // 학년

    private String school; //학교 (관리자가 입력)
    private String major; //학과 (관리자가 입력)

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TechStack> techStacks;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Certificate> certificates;

    @ElementCollection
    @CollectionTable(name = "member_awards", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "award_file_name")
    private Set<String> awardFileNames;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ContestEntry> contestEntries;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private int warningCount;

    @Enumerated(EnumType.STRING)
    private SuspensionStatus suspensionStatus;

    private LocalDateTime suspensionEndTime;
    /*
    public void increaseWarningCount() {
        this.warningCount++;
    }

    public void setSuspensionStatus(SuspensionStatus status) {
        this.suspensionStatus = status;
    }

    public void setSuspensionEndTime(LocalDateTime endTime) {
        this.suspensionEndTime = endTime;
    }
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public MemberDomain toDomain() {
        return new MemberDomain(
                id,
                new MemberInfo(
                        email,
                        name,
                        profileImage,
                        memberRole,
                        memberField,
                        memberDuty,
                        grade,
                        school,
                        major,
                        techStacks,
                        certificates,
                        awardFileNames,
                        contestEntries
                ),
                createdAt,
                updatedAt,
                warningCount,
                suspensionStatus,
                suspensionEndTime
        );
    }

    public static Member from(MemberDomain domain) {
        return Member.builder()
                .id(domain.getId())
                .email(domain.getMemberInfo().email())
                .name(domain.getMemberInfo().name())
                .memberRole(domain.getMemberInfo().memberRole())
                .profileImage(domain.getMemberInfo().profileImage())
                .memberField(domain.getMemberInfo().memberField())
                .memberDuty(domain.getMemberInfo().memberDuty())
                .grade(domain.getMemberInfo().grade())
                .school(domain.getMemberInfo().school())
                .major(domain.getMemberInfo().major())
                .techStacks(domain.getMemberInfo().techStacks())
                .certificates(domain.getMemberInfo().certificates())
                .contestEntries(domain.getMemberInfo().contestEntries())
                .awardFileNames(domain.getMemberInfo().awards())
                .warningCount(domain.getWarningCount())
                .suspensionStatus(domain.getStatus())
                .suspensionEndTime(domain.getSuspensionEndTime())
                .createdAt(domain.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

    }
    public static Member from(Long userId) {
        return Member.builder()
                .id(userId)
                .build();
    }
}
