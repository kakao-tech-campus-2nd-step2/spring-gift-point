package gift.domain.repository;

import gift.domain.entity.LocalMember;
import gift.domain.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalMemberRepository extends JpaRepository<LocalMember, Long> {

    Optional<LocalMember> findByMember(Member member);
}
