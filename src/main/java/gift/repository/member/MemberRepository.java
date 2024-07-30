package gift.repository.member;

import gift.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByEmail(String email);

    Optional<Member> findMemberByEmailAndPassword(String email, String password);

    @Query(
            "select m from Member m" +
                    " join fetch m.wishList" +
                    " where m.email=:email "
    )
    Optional<Member> findMemberWithRelation(@Param("email") String email);

    Optional<Member> findMemberByKakaoId(String kakaoId);
}
