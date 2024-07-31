package gift.doamin.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.doamin.category.entity.Category;
import gift.doamin.category.repository.JpaCategoryRepository;
import gift.doamin.product.entity.Product;
import gift.doamin.user.entity.User;
import gift.doamin.user.entity.UserRole;
import gift.doamin.user.repository.JpaUserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class JpaProductRepositoryTest {

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaCategoryRepository categoryRepository;

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

        Product savedProduct = jpaProductRepository.save(product);

        assertThat(savedProduct.getId()).isNotNull();
    }

    @Test
    void findAll() {
        User user1 = jpaUserRepository.findByEmail("test1@test.com").get();
        Category category = categoryRepository.findById(1L).get();
        Product product1 = new Product(user1, category, "test1", 10000, "test1.png");
        jpaProductRepository.save(product1);
        User user2 = jpaUserRepository.findByEmail("test2@test.com").get();
        Product product2 = new Product(user2, category, "test2", 10000, "test.png2");
        jpaProductRepository.save(product2);

        List<Product> allProducts = jpaProductRepository.findAll();

        assertThat(allProducts.size()).isEqualTo(2);
    }

    @Test
    void findById() {
        User user = jpaUserRepository.findByEmail("test1@test.com").get();
        Category category = categoryRepository.findById(1L).get();
        Product product = new Product(user, category, "test", 10000, "test.png");
        Product savedProduct = jpaProductRepository.save(product);

        Optional<Product> foundProduct = jpaProductRepository.findById(savedProduct.getId());

        assertThat(foundProduct.get()).isEqualTo(savedProduct);
    }

    @Test
    void deleteById() {
        User user = jpaUserRepository.findByEmail("test1@test.com").get();
        Category category = categoryRepository.findById(1L).get();
        Product product = new Product(user, category, "test", 10000, "test.png");
        Product savedProduct = jpaProductRepository.save(product);

        jpaProductRepository.deleteById(savedProduct.getId());
        Optional<Product> foundProduct = jpaProductRepository.findById(savedProduct.getId());

        assertThat(foundProduct.isEmpty()).isTrue();
    }

}