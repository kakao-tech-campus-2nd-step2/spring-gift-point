package gift.repository.member;

import gift.model.member.Member;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    void deleteById(Long id);
}
