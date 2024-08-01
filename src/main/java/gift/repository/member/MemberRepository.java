package gift.repository.member;

import gift.model.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepository {

    Member save(Member member);

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    void deleteById(Long id);

    Page<Member> findAll(Pageable pageable);

}
