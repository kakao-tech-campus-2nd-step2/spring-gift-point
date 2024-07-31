package gift.repository;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findWishByMemberId(Long memberId);

    Optional<Wish> findWishByMember_IdAndProduct(Long memberId, Product product);
}
