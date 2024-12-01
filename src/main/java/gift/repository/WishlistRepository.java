package gift.repository;

import gift.vo.Member;
import gift.vo.Product;
import gift.vo.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findByMemberId(Long memberId, Pageable pageable);
    Optional<Wish> findByMemberAndProduct(Member member, Product product);
}
