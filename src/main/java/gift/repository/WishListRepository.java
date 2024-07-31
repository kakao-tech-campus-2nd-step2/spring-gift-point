package gift.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gift.entity.WishList;
import java.util.Optional;

@Repository
public interface WishListRepository  extends JpaRepository<WishList, Long>{

    Page<WishList> findByMemberIdOrderByProductIdDesc(Pageable pageable, Long memberId);
    List<WishList> findByMemberId(Long memberId);
    List<WishList> findByProductId(Long productId);
    Optional<WishList> findByMemberIdAndProductId(Long memberId, Long productId);
}
