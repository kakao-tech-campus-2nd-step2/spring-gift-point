package gift.repositories;


import gift.domain.Wish;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    // 특정 회원의 Wishlist 조회
    Page<Wish> findAllByMemberId(Long memberId, Pageable pageable);

    List<Wish> findAllByMemberId(Long memberId);

    // 특정 회원의 특정 상품에 대한 Wish 삭제
    void deleteByMemberIdAndProductId(Long memberId, Long productId);


}
