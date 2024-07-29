package gift.repository;

import gift.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Member save(Member member);
    void delete(Member member);
    boolean existsByEmail(String email);
    Optional<Member> findByToken(String token);
}
