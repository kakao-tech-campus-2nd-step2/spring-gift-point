package gift.repository;

import gift.domain.WishProduct;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishProductRepository extends JpaRepository<WishProduct, Long> {

    Page<WishProduct> findByMemberId(Long memberId, Pageable pageable);

    Optional<WishProduct> findByMemberIdAndProductId(Long memberId, Long productId);

    void deleteAllByProductId(Long productId);

    void deleteAllByMemberId(Long memberId);
}
