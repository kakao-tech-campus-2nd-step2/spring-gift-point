package gift.Repository;

import gift.Model.Entity.Member;
import gift.Model.Value.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(Email email);
}