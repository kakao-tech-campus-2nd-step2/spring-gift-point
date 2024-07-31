package gift.service;

import gift.entity.Product;
import gift.entity.ProductWishlist;
import gift.entity.Wishlist;
import gift.entity.WishlistDTO;
import gift.exception.ResourceNotFoundException;
import gift.repository.ProductRepository;
import gift.repository.ProductWishlistRepository;
import gift.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final ProductWishlistRepository productWishlistRepository;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository, ProductWishlistRepository productWishlistRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.productWishlistRepository = productWishlistRepository;
    }

    public Page<Product> getWishlistProducts(String email, Pageable pageable) {
        Optional<Wishlist> wishlist = wishlistRepository.findByEmail(email);
        if (wishlist.isEmpty()) {
            wishlist = Optional.of(wishlistRepository.save(new Wishlist(email)));
        }

        Page<ProductWishlist> productWishlists = productWishlistRepository.findByWishlistId(wishlist.get().getId(), pageable);
        List<Product> products = productWishlists.stream()
                .map(productWishlist -> productWishlist.getProduct())
                .collect(Collectors.toList());

        return new PageImpl<>(products, pageable, productWishlists.getTotalElements());
    }

    public List<Product> getWishlistProducts(String email) {
        Optional<Wishlist> wishlist = wishlistRepository.findByEmail(email);
        if (wishlist.isEmpty()) {
            wishlist = Optional.of(wishlistRepository.save(new Wishlist(email)));
        }

        List<ProductWishlist> productWishlists = productWishlistRepository.findByWishlistId(wishlist.get().getId());
        List<Product> products = productWishlists.stream()
                .map(productWishlist -> productWishlist.getProduct())
                .collect(Collectors.toList());

        return products;
    }

    @Transactional
    public void addWishlistProduct(String email, WishlistDTO wishlistDTO) {
        Long id = wishlistDTO.getProductId();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        Optional<Wishlist> wishlist = wishlistRepository.findByEmail(email);

        if (wishlist.isEmpty()) {
            Wishlist saved = wishlistRepository.save(new Wishlist(email));
            wishlist = Optional.of(saved);
        }

        ProductWishlist productWishlist = new ProductWishlist(product, wishlist.get());
        productWishlistRepository.save(productWishlist);

    }

    @Transactional
    public void deleteWishlist(String email, Long productId) {
        Optional<Wishlist> wishlist = wishlistRepository.findByEmail(email);
        if (wishlist.isEmpty()) {
            return;
        }
        productWishlistRepository.deleteByProductIdAndWishlistId(productId, wishlist.get().getId());
    }
}
