package core.contest5.member.service;


public interface MemberRepository {

    Long save(MemberInfo memberInfo);

    MemberDomain findByEmail(String email);

    MemberDomain findById(Long memberId);

    void update(MemberDomain domain);
    Long countMembersJoinedToday();
}
