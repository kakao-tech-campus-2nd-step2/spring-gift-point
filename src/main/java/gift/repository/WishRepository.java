package gift.repository;

import gift.entity.Product;
import gift.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByMemberId(Long memberId);
    //List<Wish> findByProductId(Long productId);
    //Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);
    //void deleteByMemberIdAndProductId(Long memberId, Long productId);
    //boolean existsByMemberIdAndOptionId(Long memberId, Long optionId);
    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);
    Optional<Wish> findByIdAndMemberId(Long wishId, Long memberId);
    Page<Wish> findByMemberId(Long memberId, Pageable pageable);
}
