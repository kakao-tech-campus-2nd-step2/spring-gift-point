package gift.repository.wish;

import gift.model.gift.Gift;
import gift.model.user.User;
import gift.model.wish.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findByUser(User user, Pageable pageable);

    Optional<Wish> findByUserAndGift(User user, Gift gift);

    void deleteByUserAndGift(User user, Gift gift);
}
