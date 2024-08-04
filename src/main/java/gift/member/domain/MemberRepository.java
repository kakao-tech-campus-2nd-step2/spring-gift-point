package gift.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findByKakaoId(Long kakaoId);

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
