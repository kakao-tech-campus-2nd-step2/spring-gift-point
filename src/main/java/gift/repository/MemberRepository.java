package gift.repository;

import gift.domain.member.Member;
import gift.domain.member.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findById(Long memberId);

    boolean existsByEmail(String email);

    Optional<Member> findBySocialAccount_SocialIdAndSocialAccount_SocialType(Long socialId, SocialType socialType);

}
