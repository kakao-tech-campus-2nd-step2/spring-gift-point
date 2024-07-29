package gift.repository.gift;

import gift.model.gift.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {

}
