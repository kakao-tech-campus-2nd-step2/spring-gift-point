package gift.repository;

import gift.domain.MemberDomain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findById(String id);
}
