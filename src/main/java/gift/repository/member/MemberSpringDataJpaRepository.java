package gift.repository.member;

import gift.domain.LoginType;
import gift.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberSpringDataJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndLoginType(String email, LoginType loginType);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.point = m.point - :points WHERE m.id = :memberId")
    void subtractPoints(Long memberId, Integer points);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.point = m.point + :points WHERE m.id = :memberId")
    void addPoints(Long memberId, int points);
}