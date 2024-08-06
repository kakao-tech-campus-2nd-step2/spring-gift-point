package gift.repository;

import gift.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);

    default Member findByIdOrThrow(Long memberId) {
        return findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원이 존재하지 않습니다."));
    }
}
