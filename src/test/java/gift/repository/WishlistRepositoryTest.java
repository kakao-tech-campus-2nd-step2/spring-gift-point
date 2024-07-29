package gift.repository;

import static org.assertj.core.api.Assertions.*;

import gift.category.domain.Category;
import gift.category.repository.CategoryRepository;
import gift.product.domain.Product;
import gift.product.repository.ProductRepository;
import gift.user.domain.User;
import gift.user.repository.UserRepository;
import gift.wish.domain.WishlistItem;
import gift.wish.repository.WishlistRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class WishlistRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private WishlistRepository wishlistRepository;
    @BeforeEach
    public void setUp() {
        //clear
        userRepository.deleteAll();
        productRepository.deleteAll();
        wishlistRepository.deleteAll();
        categoryRepository.deleteAll();

        //User
        User user1 = new User(1L, "abc", "123");
        User user2 = new User(2L, "qwer", "5678");
        userRepository.save(user1);
        userRepository.save(user2);
        //Category
        Category category1 = new Category("name");
        categoryRepository.save(category1);
        //Product
        Product product1 = new Product(1L, "water", 1000L, "www.naver.com");
        product1.setCategory(categoryRepository.findById(1L).get());
        Product product2 = new Product(2L, "cola", 3000L, "www.coke.com");
        product2.setCategory(categoryRepository.findById(1L).get());
        productRepository.save(product1);
        productRepository.save(product2);

        //Wishlist
        WishlistItem wishlistItem1 = new WishlistItem();
        wishlistItem1.setUser(user1);
        wishlistItem1.setProduct(product1);
        wishlistItem1.setAmount(1);

        WishlistItem wishlistItem2 = new WishlistItem();
        wishlistItem2.setUser(user1);
        wishlistItem2.setProduct(product2);
        wishlistItem2.setAmount(2);

        WishlistItem wishlistItem3 = new WishlistItem();
        wishlistItem3.setUser(user2);
        wishlistItem3.setProduct(product1);
        wishlistItem3.setAmount(5);

        wishlistRepository.save(wishlistItem1);
        wishlistRepository.save(wishlistItem2);
        wishlistRepository.save(wishlistItem3);
    }

    @Test
    public void testFindListByUserId() {
        Pageable pageable = PageRequest.of(0, 10);
        // then
        Page<WishlistItem> user1Wishlist = wishlistRepository.findListByUserId(1L, pageable);
        assertThat(user1Wishlist).hasSize(2);

        Page<WishlistItem> user2Wishlist = wishlistRepository.findListByUserId(2L, pageable);
        assertThat(user2Wishlist).hasSize(1);
    }
}
