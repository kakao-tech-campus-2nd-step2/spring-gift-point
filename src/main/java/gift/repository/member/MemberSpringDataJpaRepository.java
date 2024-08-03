package gift.repository.member;

import gift.domain.LoginType;
import gift.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberSpringDataJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndLoginType(String email, LoginType loginType);
    void subtractPoints(Long memberId, Integer point);
}