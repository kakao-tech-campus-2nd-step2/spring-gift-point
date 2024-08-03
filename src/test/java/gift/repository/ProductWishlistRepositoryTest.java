package gift.repository;

import gift.entity.middle.ProductWishlist;
import gift.entity.product.Product;
import gift.entity.wishlist.Wishlist;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class ProductWishlistRepositoryTest {

    private Product product;
    private Wishlist wishlist;
    private ProductWishlist productWishlist;

    @Autowired
    private ProductWishlistRepository productWishlistRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WishlistRepository wishlistRepository;

    @BeforeEach
    void setUp() {
        product = productRepository.save(new Product());
        wishlist = wishlistRepository.save(new Wishlist());
        productWishlist = productWishlistRepository.save(new ProductWishlist(product, wishlist));
    }

    @Test
    void deleteByProductIdAndWishlistId_테스트() {
        // given
        // when
        productWishlistRepository.deleteByProductIdAndWishlistId(product.getId(), wishlist.getId());

        // then
        List<ProductWishlist> expect = productWishlistRepository
                .findByProductIdAndWishlistId(product.getId(), wishlist.getId());
        Assertions.assertThat(expect.size()).isEqualTo(0);
    }
}
