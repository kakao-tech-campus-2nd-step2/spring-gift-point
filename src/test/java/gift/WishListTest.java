package gift;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gift.model.Name;
import gift.model.Product;
import gift.model.User;
import gift.model.WishList;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishListRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class WishListTest {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenWishListIsCreated_thenWishListSavedCorrectly() {
        User user = new User(null, "test@example.com", "password");
        userRepository.save(user);

        Product product = new Product(null, new Name("TestProduct"), 100, "http://example.com/image.png", 1L, new ArrayList<>());
        productRepository.save(product);

        WishList wishList = new WishList(null, user, product);
        WishList savedWishList = wishListRepository.save(wishList);

        assertAll(
            () -> assertNotNull(savedWishList.getId()),
            () -> assertEquals(user, savedWishList.getUser()),
            () -> assertEquals(product, savedWishList.getProduct())
        );
    }
}