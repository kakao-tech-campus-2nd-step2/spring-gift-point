package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.model.Category;
import gift.model.Product;
import gift.model.User;
import gift.model.Wish;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("/truncate.sql")
public class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("위시 리스트 등록")
    void register() {
        Category category = new Category(null, "차량", "brown", "www.aaa.jpg", "차량 카테고리입니다.");
        categoryRepository.save(category);

        User user = new User(null, "yso3865", "yso8296@gmail.com");
        Product product = new Product(null, "product1", 1000, "image1.jpg", category);
        userRepository.save(user);
        productRepository.save(product);

        Wish wish = new Wish(null, user, product, 3);

        Wish actual = wishRepository.save(wish);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getProduct()).isEqualTo(wish.getProduct()),
            () -> assertThat(actual.getUser()).isEqualTo(wish.getUser()),
            () -> assertThat(actual.getCount()).isEqualTo(wish.getCount())
        );
    }

    @Test
    @DisplayName("위시 리스트 조회")
    void findWish() {
        Category category = new Category(null, "차량", "brown", "www.aaa.jpg", "차량 카테고리입니다.");
        categoryRepository.save(category);

        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by("id").descending());
        User user = new User(null, "yso3865", "yso8296@gmail.com");
        Product product = new Product(null, "product1", 1000, "image1.jpg", category);
        userRepository.save(user);
        productRepository.save(product);

        Wish wish = new Wish(null, user, product, 3);
        wishRepository.save(wish);

        Page<Wish> wishes = wishRepository.findByUserId(user.getId(), pageRequest);

        assertThat(wishes).isNotNull();
    }

    @Test
    @DisplayName("위시 리스트 삭졔")
    void delete() {
        Category category = new Category(null, "차량", "brown", "www.aaa.jpg", "차량 카테고리입니다.");
        categoryRepository.save(category);

        User user = new User(null, "yso3865", "yso8296@gmail.com");
        Product product = new Product(null, "product1", 1000, "image1.jpg", category);
        userRepository.save(user);
        productRepository.save(product);

        Wish wish = new Wish(null, user, product, 3);
        wishRepository.save(wish);

        wishRepository.deleteByProductIdAndUserId(product.getId(), user.getId());
        Optional<Wish> wishes = wishRepository.findByProductIdAndUserId(product.getId(), user.getId());

        assertThat(wishes).isEmpty();
    }
}
