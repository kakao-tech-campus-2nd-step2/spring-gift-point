package gift.product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import gift.domain.AppUser;
import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.domain.Role;
import gift.domain.Wish;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OptionRepositoryTest {
    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WishListRepository wishListRepository;

    private AppUser appUser;
    private Product product;
    private Wish wish;
    private Category category;

    @BeforeEach
    public void setUp() {
        appUser = new AppUser("aabb@kakao.com", "1234", Role.USER, "aaaa");
        category = new Category("기타", "");
        product = new Product("Test", 1000, "url", appUser, category);
        wish = new Wish(appUser, product, 5);
        appUser = userRepository.save(appUser);
        category = categoryRepository.save(category);
        product = productRepository.save(product);
        wishListRepository.save(wish);
    }

//    @Test
    @DisplayName("같은 상품 내에서 기존 옵션 이름과 중복되는 옵션을 추가할 수 없다")
    public void addOption_ShouldNotAddOption_WhenDuplicateName() {
        Option option = new Option("Option 1", 10,  product);
        optionRepository.save(option);

        Option option2 = new Option("Option 1", 10,  product);
        assertThatThrownBy(() ->
                optionRepository.save(option2)
        ).isInstanceOf(DataIntegrityViolationException.class);
    }

}
