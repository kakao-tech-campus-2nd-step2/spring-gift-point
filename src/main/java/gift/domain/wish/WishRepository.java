package gift.domain.wish;

import gift.domain.product.Product;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    @EntityGraph(attributePaths = {"member", "product"})
    Page<Wish> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"member", "product"})
    Page<Wish> findAllByMemberId(Long memberId, Pageable pageable);

    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);

    boolean existsByMemberEmailAndProductId(String memberEmail, Long productId);
}
