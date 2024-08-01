package gift.repository;

import gift.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findById(int id);
    int searchIdByToken(String token);
    Boolean existsByToken(String token);
    Member save(Member member);
}