//package gift.repository;
//
//import gift.model.Product;
//import gift.model.User;
//import gift.model.Wish;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//class WishRepositoryTest {
//    @Autowired
//    private WishRepository wishRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    private User user1;
//    private User user2;
//
//    private Product product1;
//    private Product product2;
//    private Product product3;
//
//    private Wish wish1;
//
//
//    @BeforeEach
//    void setUp() {
//        wishRepository.deleteAll();
//        userRepository.deleteAll();
//        productRepository.deleteAll();
//
//        user1 = userRepository.save(new User("user1@test.com", "password1", "User One"));
//        user2 = userRepository.save(new User("user2@test.com", "password2", "User Two"));
//
//        product1 = productRepository.save(new Product("cake", 4500L, "www.cake.com"));
//        product2 = productRepository.save(new Product("cookie", 2000L, "www.cookie.com"));
//        product3 = productRepository.save(new Product("pie", 5000L, "www.pie.com"));
//
//        wish1 = wishRepository.save(new Wish(user1, product1));
//    }
//
//
//    @Test
//    void save() {
//        User user = userRepository.findById(user1.getId()).orElse(null);
//        Product product = productRepository.findById(product3.getId()).orElse(null);
//        Wish expected = new Wish(user, product);
//
//        Wish actual = wishRepository.save(expected);
//        assertAll(
//                () -> assertThat(actual.getId()).isNotNull(),
//                () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
//                () -> assertThat(actual.getProductId()).isEqualTo(expected.getProductId())
//        );
//    }
//
//    @Test
//    void findProductsByUserId() {
//        List<Long> productIds = wishRepository.findProductIdsByUserId(user1.getId());
//
//        assertThat(productIds).hasSize(2);
//        assertThat(productIds).contains(product1.getId(), product2.getId());
//    }
//
//    @Test
//    void deleteByUserIdAndProductId() {
//        wishRepository.deleteByUserAndProduct(user1, product1);
//        Optional<Wish> deletedWish = wishRepository.findById(wish1.getId());
//
//        assertThat(deletedWish).isEmpty();
//    }
//}