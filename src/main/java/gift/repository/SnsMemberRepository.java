package gift.repository;

import gift.entity.SnsMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnsMemberRepository extends JpaRepository<SnsMember,Long> {

    public Optional<SnsMember> findByOauthId(Long oauthId);
    public Optional<SnsMember> findByEmail(String email);


}
