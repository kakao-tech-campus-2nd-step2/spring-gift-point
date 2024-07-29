package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import gift.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;
    private Product product;
    private Option option;
    private User user;

    @BeforeEach
    void setUp() {
        category = categoryRepository.save(
            new Category("testName", "testColor", "testImage", "testDescription"));
        product = productRepository.save(new Product("testName", 1000, "testImage.jpg", category));
        option = optionRepository.save(new Option("testOption", 300, product));
        user = userRepository.save(new User("test@abc.com", "password"));
    }

    @Test
    @DisplayName("create 테스트")
    void createTest() {
        Order expected = new Order(option, user, 30, LocalDateTime.now(), "test");

        Order Actual = orderRepository.save(expected);

        assertThat(Actual.getId()).isNotNull();
        assertThat(Actual.getMessage()).isEqualTo("test");
    }

    @Test
    @DisplayName("findAllByUserId 테스트")
    void findAllByUserIdTest() {
        Order expected = orderRepository.save(
            new Order(option, user, 30, LocalDateTime.now(), "test"));

        List<Order> orders = orderRepository.findAllByUserId(user.getId());
        Order actual = orders.getFirst();

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getMessage()).isEqualTo("test");
    }

}
