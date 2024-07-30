package gift.domain.wishlist.service;

import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductJpaRepository;
import gift.domain.user.entity.User;
import gift.domain.wishlist.dto.WishItemRequestDto;
import gift.domain.wishlist.dto.WishItemResponseDto;
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
    public WishItemResponseDto create(WishItemRequestDto wishItemRequestDto, User user) {
        Product product = productJpaRepository.findById(wishItemRequestDto.productId())
            .orElseThrow(() -> new InvalidProductInfoException("error.invalid.product.id"));

        WishItem wishItem = wishItemRequestDto.toWishItem(user, product);
        WishItem savedWishItem = wishlistJpaRepository.save(wishItem);

        return WishItemResponseDto.from(savedWishItem);
    }

    public Page<WishItemResponseDto> readAll(Pageable pageable, User user) {
        Page<WishItem> foundWishlist = wishlistJpaRepository.findAllByUserId(user.getId(), pageable);

        if (foundWishlist == null) {
            return Page.empty();
        }
        return foundWishlist.map(WishItemResponseDto::from);
    }

    public void delete(long wishItemId) {
        WishItem wishItem = wishlistJpaRepository.findById(wishItemId)
            .orElseThrow(() -> new InvalidProductInfoException("error.invalid.product.id"));

        wishlistJpaRepository.delete(wishItem);
    }

    public void deleteAllByUserId(User user) {
        wishlistJpaRepository.deleteAllByUserId(user.getId());
    }
}
