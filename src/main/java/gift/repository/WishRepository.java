package gift.repository;

import gift.model.Wish;
import gift.model.Member;
import gift.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findAllByMemberId(Long memberId, Pageable pageable);
    Optional<Wish> findByIdAndMemberId(Long id, Long memberId);
    void deleteByMemberAndProduct(Member member, Product product);
}
