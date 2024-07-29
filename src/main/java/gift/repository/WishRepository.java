package gift.repository;

import gift.domain.Product;
import gift.domain.Wish;
import gift.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {

    boolean existsByMemberIdAndProductId(Long memberId, Long productId);

    Page<Wish> findByMember(Member member, Pageable pageable);

    Optional<Wish> findByMemberAndProduct(Member member, Product product);

}
