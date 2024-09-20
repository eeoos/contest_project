package core.contest5.member.repository;

import core.contest5.member.domain.memberinfo.MemberField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberFieldRepository extends JpaRepository<MemberField, Long> {
    Optional<MemberField> findByFieldName(String fieldName);
}
