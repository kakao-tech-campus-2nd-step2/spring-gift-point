package gift.service;

import gift.dto.ProductDTO;
import gift.model.Product;
import gift.model.User;
import gift.model.Wishlist;
import gift.repository.WishlistRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void addWishlist(User user, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productId));

        Wishlist wishlist = new Wishlist(user, product);
        wishlistRepository.save(wishlist);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductsFromWishlist(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return wishlistRepository.findByUserEmail(user.getEmail(), pageable)
                .map(wishlist -> new ProductDTO(wishlist.getProduct()));
    }

    @Transactional
    public void deleteWishlist(User user, Long productId) {
        wishlistRepository.deleteByUserEmailAndProductId(user.getEmail(), productId);
    }
}