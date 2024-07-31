package gift.member.persistence;

import gift.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsernameAndPassword(String username, String password);

    Optional<Member> findByUsername(String username);
}
