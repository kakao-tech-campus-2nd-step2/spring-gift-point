package gift.repository;

import gift.model.Product;
import gift.model.Wish;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishRepository extends JpaRepository<Wish, Long> {

    @Query("select p from Product p where p.id in (select w.product.id from Wish w where w.member.id = :memberId)")
    List<Product> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("select w.product from Wish w where w.member.id =:memberId")
    Page<Product> findPageBy(@Param("memberId") Long memberId, Pageable pageable);

    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);
}
