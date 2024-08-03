package gift.wish.repository;

import gift.product.entity.Product;
import gift.wish.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {

  @Query("SELECT w FROM Wish w WHERE w.user.email = :email")
  Page<Wish> findByUserEmail(@Param("email") String email, Pageable pageable);

  void deleteByUserIdAndProduct(Long userId, Product product);

  Optional<Wish> findByUserIdAndProduct(Long userId, Product product);

  List<Wish> findByProductId(Long productId);

  Page<Wish> findByUserId(Long userId, Pageable pageable);

  Optional<Wish> findByIdAndUserId(Long id, Long userId);
  @Transactional
  @Modifying
  @Query("DELETE FROM Wish w WHERE w.product.id = :productId")
  void deleteAllByProductId(Long productId);
}
