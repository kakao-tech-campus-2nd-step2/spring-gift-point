package gift.member.repository;

import gift.global.MyCrudRepository;
import gift.member.domain.Email;
import gift.member.domain.Member;
import gift.member.domain.Nickname;
import gift.member.domain.Password;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends MyCrudRepository<Member, Long> {
    Optional<Member> findByEmailAndPassword(Email email, Password password);

    Optional<Member> findByEmail(Email email);

    boolean existsById(Long id);

    boolean existsByEmail(Email email);

    boolean existsByNickname(Nickname nickname);

    @Query("SELECT m.accessToken FROM Member m WHERE m.id = :id")
    String findAccessTokenById(@Param("id") Long id);
}
