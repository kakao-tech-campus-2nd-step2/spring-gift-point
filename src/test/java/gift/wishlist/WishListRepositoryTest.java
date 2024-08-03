package gift.wishlist;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.AppUser;
import gift.domain.Category;
import gift.domain.Product;
import gift.domain.Role;
import gift.domain.Wish;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishListRepository;
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
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private AppUser appUser;
    private Product product;
    private Category category;

//    @BeforeEach
    public void setUp() {
        appUser = new AppUser("aabb@kakao.com", "1234", Role.USER, "aaaa");
        category = new Category("기타", "");
        product = new Product("Test", 1000, "url", appUser, category);

        appUser = userRepository.save(appUser);
        category = categoryRepository.save(category);
        product = productRepository.save(product);

        Wish wish = new Wish(appUser, product, 1);
        wishListRepository.save(wish);
    }

//    @Test
    public void testFindWishesByUserId() {
        System.out.println("appUser = " + appUser.getId());
        // 테스트 시작
        List<Wish> results = wishListRepository.findWishesByAppUserId(appUser.getId());

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getProduct().getId()).isEqualTo(product.getId());
    }
}
