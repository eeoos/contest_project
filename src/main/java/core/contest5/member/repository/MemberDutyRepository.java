package core.contest5.member.repository;

import core.contest5.member.domain.memberinfo.MemberDuty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberDutyRepository extends JpaRepository<MemberDuty, Long> {

    Optional<MemberDuty> findByDutyName(String name);
}
