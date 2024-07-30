package gift.member.persistence.repository;

import gift.global.exception.ErrorCode;
import gift.global.exception.custrom.NotFoundException;
import gift.member.persistence.entity.Wishlist;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class WishlistRepositoryImpl implements WishlistRepository {

    private final WishlistJpaRepository wishlistJpaRepository;

    public WishlistRepositoryImpl(WishlistJpaRepository wishlistJpaRepository) {
        this.wishlistJpaRepository = wishlistJpaRepository;
    }

    @Override
    public Long saveWishList(Wishlist wishList) {
        return wishlistJpaRepository.save(wishList).getId();
    }

    @Override
    public Wishlist getWishListByMemberIdAndProductId(Long memberId, Long productId) {
        return wishlistJpaRepository.findByMemberIdAndProductId(memberId, productId)
            .orElseThrow(() -> new NotFoundException(
                ErrorCode.DB_NOT_FOUND,
                "Wishlist with member id " + memberId + " and product id " + productId
                    + " not found"
            ));
    }

    @Override
    public Long updateWishlist(Wishlist wishList) {
        return wishlistJpaRepository.save(wishList).getId();
    }

    @Override
    public void deleteWishlist(Long wishId) {
        wishlistJpaRepository.deleteById(wishId);
    }

    @Override
    public void deleteWishlistByMemberIdAndProductId(Long memberId, Long productId) {
        try {
            wishlistJpaRepository.deleteByMemberIdAndProductId(memberId, productId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(
                ErrorCode.DB_NOT_FOUND,
                "Wishlist with member id " + memberId + " and product id " + productId
                    + " not found"
            );
        }
    }

    @Override
    public void deleteAll() {
        wishlistJpaRepository.deleteAll();
    }

    @Override
    public Page<Wishlist> getWishListByPage(Long memberId, Pageable pageRequest) {
        return wishlistJpaRepository.findByMemberId(memberId, pageRequest);
    }
}
