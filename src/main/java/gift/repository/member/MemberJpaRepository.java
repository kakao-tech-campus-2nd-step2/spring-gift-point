package gift.repository.member;

import gift.model.member.Member;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface MemberJpaRepository extends JpaRepository<Member, Long>, MemberRepository {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

}
