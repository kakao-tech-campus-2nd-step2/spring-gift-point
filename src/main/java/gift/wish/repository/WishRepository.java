package gift.wish.repository;

import gift.product.entity.Product;
import gift.wish.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {

  List<Wish> findByUserId(Long userId);

  void deleteByUserIdAndProduct(Long userId, Product product);

  Optional<Wish> findByUserIdAndProduct(Long userId, Product product);

  List<Wish> findByProductId(Long productId);

  Page<Wish> findByUserId(Long userId, Pageable pageable);

  @Transactional
  @Modifying
  @Query("DELETE FROM Wish w WHERE w.product.id = :productId")
  void deleteAllByProductId(Long productId);

  void deleteByProductId(Long productId);
}
