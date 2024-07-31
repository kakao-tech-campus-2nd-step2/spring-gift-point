package gift.repository;

import gift.model.product.Product;
import gift.model.wish.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findAll(Pageable pageable);
    Page<Wish> findAllByMemberId(Long memberId, Pageable pageable);
    Optional<Wish> findByProductId(Long productId);
}
