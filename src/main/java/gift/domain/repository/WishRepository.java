package gift.domain.repository;

import gift.domain.model.entity.Product;
import gift.domain.model.entity.User;
import gift.domain.model.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    @Query("SELECT w FROM Wish w JOIN FETCH w.product WHERE w.user.email = :email")
    Page<Wish> findByUserEmail(String email, Pageable pageable);

    Optional<Wish> findByUserEmailAndProductId(String email, Long productId);

    boolean existsByUserEmailAndProductId(String email, Long productId);

    void deleteByUserEmailAndProductId(String email, Long productId);

    void deleteByUserAndProduct(User user, Product product);
}