package core.contest5.member.repository;

import core.contest5.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
//    boolean existsByEmail(String email);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.createdAt >= :startOfDay")
    Long countMembersJoinedToday(LocalDateTime startOfDay);
}
