package gift.wishlist;

import static org.assertj.core.api.Assertions.assertThat;

import gift.product.model.ProductRepository;
import gift.product.model.dto.Product;
import gift.user.model.UserRepository;
import gift.user.model.dto.AppUser;
import gift.user.model.dto.Role;
import gift.wishlist.model.WishListRepository;
import gift.wishlist.model.dto.Wish;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WishListRepositoryTest {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private AppUser appUser;
    private Product product;

    @BeforeEach
    public void setUp() {
        appUser = new AppUser("aabb@kakao.com", "1234", Role.USER, "aaaa");
        product = new Product("Test", 1000, "url", appUser);

        appUser = userRepository.save(appUser);
        product = productRepository.save(product);

        Wish wish = new Wish(appUser, product, 1);
        wishListRepository.save(wish);
    }

    @Test
    public void testFindWishesByUserId() {
        System.out.println("appUser = " + appUser.getId());
        // 테스트 시작
        List<Wish> results = wishListRepository.findWishesByAppUserIdAndIsActiveTrue(appUser.getId());

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getProduct().getId()).isEqualTo(product.getId());
    }
}
