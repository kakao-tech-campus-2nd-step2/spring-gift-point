package gift.user.infra;

import gift.user.domain.UserPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointJpaRepository extends JpaRepository<UserPoint, Long> {

}
