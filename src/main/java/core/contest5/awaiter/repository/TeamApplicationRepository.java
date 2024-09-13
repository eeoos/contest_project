package core.contest5.awaiter.repository;

import core.contest5.awaiter.domain.TeamApplication;
import core.contest5.member.domain.Member;
import core.contest5.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamApplicationRepository extends JpaRepository<TeamApplication, Long> {
    Optional<TeamApplication> findByPostAndApplicantAndAwaiter(Post post, Member applicant, Member awaiter);
}