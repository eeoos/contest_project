package core.contest5.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.Collection;
import java.util.Collections;

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

    public MemberDomain toDomain() {
        return new MemberDomain(
                id,
                new MemberInfo(
                        email,
                        name,
                        profileImage,
                        memberRole
                )
        );
    }

    public static Member from(MemberDomain domain) {
        return Member.builder()
                .id(domain.id())
                .email(domain.info().email())
                .name(domain.info().name())
                .memberRole(domain.info().memberRole())
                .profileImage(domain.info().profileImage())
                .build();
    }
    public static Member from(Long userId) {
        return Member.builder()
                .id(userId)
                .build();
    }
}
