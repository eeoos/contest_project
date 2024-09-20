package core.contest5.member.service;


import core.contest5.member.domain.UpdatedMemberInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberDomain{
    private Long id;
    private MemberInfo memberInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public void update(UpdatedMemberInfo updatedMemberInfo) {
        this.memberInfo = updatedMemberInfo.memberInfo();
    }
}