package gift.doamin.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.doamin.category.entity.Category;
import gift.doamin.category.repository.CategoryRepository;
import gift.doamin.product.entity.Product;
import gift.doamin.user.entity.User;
import gift.doamin.user.entity.UserRole;
import gift.doamin.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class JpaProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository jpaUserRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        jpaUserRepository.save(new User("test1@test.com", "test", "test1", UserRole.SELLER));
        jpaUserRepository.save(new User("test2@test.com", "test", "test2", UserRole.SELLER));
    }

    @Test
    void save() {
        User user = jpaUserRepository.findByEmail("test1@test.com").get();
        Category category = categoryRepository.findById(1L).get();
        Product product = new Product(user, category, "test", 10000, "test.png");

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct.getId()).isNotNull();
    }

    @Test
    void findAll() {
        User user1 = jpaUserRepository.findByEmail("test1@test.com").get();
        Category category = categoryRepository.findById(1L).get();
        Product product1 = new Product(user1, category, "test1", 10000, "test1.png");
        productRepository.save(product1);
        User user2 = jpaUserRepository.findByEmail("test2@test.com").get();
        Product product2 = new Product(user2, category, "test2", 10000, "test.png2");
        productRepository.save(product2);

        List<Product> allProducts = productRepository.findAll();

        // 목업 데이터 14개 추가
        assertThat(allProducts.size()).isEqualTo(2+14);
    }

    @Test
    void findById() {
        User user = jpaUserRepository.findByEmail("test1@test.com").get();
        Category category = categoryRepository.findById(1L).get();
        Product product = new Product(user, category, "test", 10000, "test.png");
        Product savedProduct = productRepository.save(product);

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        assertThat(foundProduct.get()).isEqualTo(savedProduct);
    }

    @Test
    void deleteById() {
        User user = jpaUserRepository.findByEmail("test1@test.com").get();
        Category category = categoryRepository.findById(1L).get();
        Product product = new Product(user, category, "test", 10000, "test.png");
        Product savedProduct = productRepository.save(product);

        productRepository.deleteById(savedProduct.getId());
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        assertThat(foundProduct.isEmpty()).isTrue();
    }

}