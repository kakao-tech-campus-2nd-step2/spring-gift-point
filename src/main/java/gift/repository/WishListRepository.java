package gift.repository;

import gift.entity.WishListEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 상품 데이터에 대한 데이터베이스 처리를 담당하는 인터페이스
 */
@Repository
public interface WishListRepository extends JpaRepository<WishListEntity, Long> {

    List<WishListEntity> findAllByMemberEntityId(Long userId, Pageable pageable);

    List<WishListEntity> findAllByMemberEntityId(Long userId);

    int deleteWishListsByMemberEntityId(Long userId);

    int deleteWishListByMemberEntityIdAndProductEntityId(Long userId, Long productId);
}
