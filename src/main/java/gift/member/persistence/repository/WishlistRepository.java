package gift.member.persistence.repository;

import gift.member.persistence.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishlistRepository {

    Long saveWishList(Wishlist wishList);
    
    Wishlist getWishListByMemberIdAndProductId(Long memberId, Long productId);

    Long updateWishlist(Wishlist wishList);

    void deleteWishlist(Long memberId, Long productId);

    void deleteAll();

    Page<Wishlist> getWishListByPage(Long memberId, Pageable pageRequest);
}
