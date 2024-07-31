package gift.repository.wish;

import gift.model.product.Product;
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

    Optional<Wish> findByUserAndProduct(User user, Product product);

    void deleteByUserAndId(User user, Long wishId);
}
