//package gift.repository;
//
//import gift.model.Product;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertAll;
//
//@DataJpaTest
//class ProductRepositoryTest {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    private Pageable pageable;
//
//    private Product product1;
//    private Product product2;
//
//    @BeforeEach
//    void setUp() {
//        productRepository.deleteAll();
//        product1 = productRepository.save(new Product("cookie", 2000L, "www.cookie.com"));
//        product2 = productRepository.save(new Product("pie", 5000L, "www.pie.com"));
//    }
//
//    @Test
//    void save() {
//        Product expected = new Product("brownie", 3000L, "www.brownie.com");
//
//        Product actual = productRepository.save(expected);
//        assertAll(
//                () -> assertThat(actual.getId()).isNotNull(),
//                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
//                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice())
//        );
//    }
//
//    @Test
//    void finAll() {
//        List<Product> products = productRepository.findAll();
//        assertThat(products).hasSize(2);
//    }
//
//    @Test
//    void findAllById() {
//        List<Long> ids = Arrays.asList(product1.getId(), product2.getId());
//        Page<Product> products = productRepository.findAllById(ids, pageable);
//
//        assertThat(products).hasSize(2).contains(product1, product2);
//    }
//
//    @Test
//    void deleteById() {
//        productRepository.deleteById(product1.getId());
//        Optional<Product> deletedProduct = productRepository.findById(product1.getId());
//
//        assertThat(deletedProduct).isEmpty();
//    }
//
//}