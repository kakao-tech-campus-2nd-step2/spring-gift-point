package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gift.model.Name;
import gift.model.Product;
import gift.repository.ProductRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenSaveProduct_thenProductIsSavedCorrectly() {
        Product expected = new Product(null, new Name("TestProduct"), 100, "http://example.com/image.png", 1L, new ArrayList<>());
        Product actual = productRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName().getName()).isEqualTo(expected.getName().getName()),
            () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
            () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl())
        );
    }

    @Test
    void givenProductId_whenFindById_thenReturnProduct() {
        Product expected = new Product(null, new Name("TestProduct"), 100, "http://example.com/image.png", 1L, new ArrayList<>());
        productRepository.save(expected);
        Optional<Product> actual = productRepository.findById(expected.getId());
        assertTrue(actual.isPresent());
        assertThat(actual.get().getName().getName()).isEqualTo(expected.getName().getName());
    }

    @Test
    void findProducts_ReturnFirstPage_WithPagination() {
        // given: 5개의 상품을 생성하고 저장
        Product product1 = new Product(null, new Name("TestProduct1"), 101, "http://example.com/image1.png", 1L, new ArrayList<>());
        Product product2 = new Product(null, new Name("TestProduct2"), 102, "http://example.com/image2.png", 1L, new ArrayList<>());
        Product product3 = new Product(null, new Name("TestProduct3"), 103, "http://example.com/image3.png", 1L, new ArrayList<>());
        Product product4 = new Product(null, new Name("TestProduct4"), 104, "http://example.com/image4.png", 1L, new ArrayList<>());
        Product product5 = new Product(null, new Name("TestProduct5"), 105, "http://example.com/image5.png", 1L, new ArrayList<>());

        productRepository.saveAll(Arrays.asList(product1, product2, product3, product4, product5));

        // when: 첫 페이지를 요청하여 3개의 아이템을 가져옴
        Pageable pageable = PageRequest.of(0, 3);
        Page<Product> productPage = productRepository.findAll(pageable);

        // then: 전체 아이템 수가 5개이고, 첫 페이지에 3개의 아이템이 있으며, 총 페이지 수가 2개인지 검증
        assertAll(
            () -> assertThat(productPage.getTotalElements()).isEqualTo(5),
            () -> assertThat(productPage.getContent()).hasSize(3),
            () -> assertThat(productPage.getTotalPages()).isEqualTo(2)
        );
    }
}