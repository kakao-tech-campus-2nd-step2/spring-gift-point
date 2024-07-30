package gift.product.infra;

import gift.product.domain.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class WishListRepository {
    private final WishListJpaRepository wishListJpaRepository;

    public WishListRepository(WishListJpaRepository wishListJpaRepository) {
        this.wishListJpaRepository = wishListJpaRepository;
    }

    public WishList findByUserId(Long userId) {
        return wishListJpaRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 위시리스트가 존재하지 않습니다."));
    }

    public Page<WishList> findByUserId(Long userId, Pageable pageable) {
        return wishListJpaRepository.findByUserId(userId, pageable);
    }

    public void save(WishList wishList) {
        wishListJpaRepository.save(wishList);
    }

    public WishList findById(Long id) {
        return wishListJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 ID의 위시리스트가 존재하지 않습니다."));
    }

    public void delete(WishList wishList) {
        wishListJpaRepository.delete(wishList);
    }
}
