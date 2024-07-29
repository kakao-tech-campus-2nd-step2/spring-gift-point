package gift.product.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gift.option.OptionFixture;
import gift.option.entity.Option;
import gift.product.ProductFixture;
import gift.product.entity.Product;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("상품 리파지토리 테스트")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // 임의의 상품 3개
        List<Product> products = List.of(
                ProductFixture.createProduct("상품1", 1000, "keyboard.png"),
                ProductFixture.createProduct("상품2", 2000, "mouse.png"),
                ProductFixture.createProduct("상품3", 3000, "monitor.png")
        );

        productRepository.saveAll(products);
    }
    
    @Test
    @DisplayName("상품 생성")
    void addProduct() {
        //given
        Product product = ProductFixture.createProduct("상품4", 4000, "headset.png");
        
        //when
        Product savedProduct = productRepository.save(product);
        
        //then
        assertAll(
                () -> assertNotNull(savedProduct.getId()),
                () -> assertEquals(product.getName(), savedProduct.getName()),
                () -> assertEquals(product.getPrice(), savedProduct.getPrice()),
                () -> assertEquals(product.getImageUrl(), savedProduct.getImageUrl())
        );
    }

    @Test
    @DisplayName("상품 조회")
    void findProduct() {
        //given
        Product product = ProductFixture.createProduct("상품5", 5000, "speaker.png");
        productRepository.save(product);

        //when
        List<Product> products = productRepository.findAll();

        //then
        assertThatList(products).hasSize(4);
        assertThatList(products).extracting(Product::getName)
                .contains("상품1", "상품2", "상품3", "상품5");
        assertThatList(products).extracting(Product::getPrice)
                .contains(1000, 2000, 3000, 5000);
        assertThatList(products).extracting(Product::getImageUrl)
                .contains("keyboard.png", "mouse.png", "monitor.png", "speaker.png");

        // 존재하지 않는 상품 조회
        assertFalse(productRepository.findById(100L).isPresent());
    }

    @Test
    @DisplayName("상품 수정")
    void updateProduct() {
        //given
        Product product = ProductFixture.createProduct("상품6", 6000, "webcam.png");
        product.addOption(OptionFixture.createOption("옵션1", 10));
        Product savedProduct = productRepository.save(product);

        //when
        savedProduct.setName("상품7");
        savedProduct.setPrice(7000);
        savedProduct.setImageUrl("camera.png");

        Product updatedProduct = productRepository.findById(savedProduct.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        //then
        assertThat(updatedProduct.getName()).isEqualTo("상품7");
        assertThat(updatedProduct.getPrice()).isEqualTo(7000);
        assertThat(updatedProduct.getImageUrl()).isEqualTo("camera.png");

        // given: 옵션 추가
        Option option = OptionFixture.createOption("옵션2", 20);
        savedProduct.addOption(option);

        // when
        Product updatedProductWithOption = productRepository.findById(savedProduct.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // then
        assertThat(updatedProductWithOption.getOptions()).hasSize(2);
        assertThat(updatedProductWithOption.getOptions()).extracting(Option::getName)
                .contains("옵션1", "옵션2");
    }

    @Test
    @DisplayName("상품 삭제")
    void deleteProduct() {
        //given
        Product product = ProductFixture.createProduct();
        Product savedProduct = productRepository.save(product);

        //when
        productRepository.deleteById(savedProduct.getId());

        //then
        assertFalse(productRepository.findById(savedProduct.getId()).isPresent());
    }
}