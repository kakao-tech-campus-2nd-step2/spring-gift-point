package gift.repository;

import gift.common.enums.LoginType;
import gift.exception.EntityNotFoundException;
import gift.model.category.Category;
import gift.model.option.Option;
import gift.model.product.Product;
import gift.model.user.User;
import gift.model.wish.Wish;
import gift.repository.product.ProductRepository;
import gift.repository.user.UserRepository;
import gift.repository.wish.WishRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "password", "testName", LoginType.DEFAULT);

        userRepository.save(user);

        Category category = new Category(10L, "test", "test", "test", "test");
        Option option1 = new Option("testOption", 1);
        List<Option> option = List.of(option1);

        product = new Product("Test Gift", 100, "test.jpg", category, option);
        productRepository.save(product);

        Wish wish = new Wish(user, product, 1);
        wishRepository.save(wish);
    }

    @Test
    @DisplayName("유저정보를 통한 위시리스트 조회가 잘 되는지 테스트")
    void testFindByUser() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Wish> wishes = wishRepository.findByUser(user, pageable);

        assertThat(wishes).isNotNull();
        assertThat(wishes.getContent()).hasSize(1);
        assertThat(wishes.getContent().get(0).getProduct().getName()).isEqualTo("Test Gift");
    }

    @Test
    @DisplayName("유저정보와 상품정보를 통한 위시리스트 조회가 잘 되는지 테스트")
    void testFindByUserAndProduct() {
        Wish wish = wishRepository.findByUserAndProduct(user, product).orElseThrow(() -> new EntityNotFoundException("해당 위시리스트 아이템을 찾을 수 없습니다."));

        assertThat(wish.getQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("위시리스트 삭제가 잘 되는지 테스트")
    void testDeleteByUserAndProduct() {
        Wish wish = wishRepository.findByUserAndProduct(user, product).orElseThrow(() -> new EntityNotFoundException("해당 위시리스트 아이템을 찾을 수 없습니다."));

        wishRepository.deleteByUserAndId(user, wish.getId());

        Optional<Wish> deletedWish = wishRepository.findByUserAndProduct(user, product);
        assertFalse(deletedWish.isPresent());
    }
}
