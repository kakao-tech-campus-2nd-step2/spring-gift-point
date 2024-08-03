package gift.domain.wishlist.service;

import gift.domain.member.entity.Member;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductJpaRepository;
import gift.domain.wishlist.dto.WishItemRequest;
import gift.domain.wishlist.dto.WishItemResponse;
import gift.domain.wishlist.entity.WishItem;
import gift.domain.wishlist.repository.WishlistJpaRepository;
import gift.exception.InvalidProductInfoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishlistService {

    private final WishlistJpaRepository wishlistJpaRepository;
    private final ProductJpaRepository productJpaRepository;

    public WishlistService(WishlistJpaRepository wishlistJpaRepository, ProductJpaRepository productJpaRepository) {
        this.wishlistJpaRepository = wishlistJpaRepository;
        this.productJpaRepository = productJpaRepository;
    }

    @Transactional
    public WishItemResponse create(WishItemRequest wishItemRequest, Member member) {
        Product product = productJpaRepository.findById(wishItemRequest.productId())
            .orElseThrow(() -> new InvalidProductInfoException("error.invalid.product.id"));

        WishItem wishItem = wishItemRequest.toWishItem(member, product);
        WishItem savedWishItem = wishlistJpaRepository.save(wishItem);

        return WishItemResponse.from(savedWishItem);
    }

    public Page<WishItemResponse> readAll(Pageable pageable, Member member) {
        Page<WishItem> foundWishlist = wishlistJpaRepository.findAllByMemberId(member.getId(), pageable);

        if (foundWishlist == null) {
            return Page.empty();
        }
        return foundWishlist.map(WishItemResponse::from);
    }

    public void delete(long wishItemId) {
        WishItem wishItem = wishlistJpaRepository.findById(wishItemId)
            .orElseThrow(() -> new InvalidProductInfoException("error.invalid.product.id"));

        wishlistJpaRepository.delete(wishItem);
    }

    public void deleteAllByMemberId(Member member) {
        wishlistJpaRepository.deleteAllByMemberId(member.getId());
    }
}
