package core.contest5.member.domain.memberinfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    GUEST,
    ADMIN,
    USER;

    private static final String PREFIX = "ROLE_";

    public String getAuthority(){
        return PREFIX + this.name();
    }
}
