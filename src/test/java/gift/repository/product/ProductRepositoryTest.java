//package gift.repository.product;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import gift.entity.ProductEntity;
//import gift.repository.ProductRepository;
//import java.util.Optional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.test.annotation.Rollback;
//
//@DataJpaTest
//class ProductRepositoryTest {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Test
//    @Rollback(false)
//    @DisplayName("상품 생성 테스트")
//    void createProductTest() {
//        ProductEntity product = new ProductEntity();
//        product.setName("Sample Product");
//        product.setPrice(1000L);
//        product.setImageUrl("http://example.com/image.jpg");
//
//        ProductEntity savedProduct = productRepository.save(product);
//
//        assertThat(savedProduct).isNotNull();
//        assertThat(savedProduct.getId()).isGreaterThan(0);
//    }
//
//    @Test
//    @DisplayName("상품 조회 테스트")
//    void findProductTest() {
//        Long productId = 1L; // Assumes a product with this ID exists
//        Optional<ProductEntity> optionalProduct = productRepository.findById(productId);
//
//        assertThat(optionalProduct).isPresent();
//        ProductEntity product = optionalProduct.get();
//        assertThat(product.getName()).isEqualTo("Sample Product");
//    }
//
//    @Test
//    @Rollback(false)
//    @DisplayName("상품 수정 테스트")
//    void updateProductTest() {
//        Long productId = 1L; // Assumes a product with this ID exists
//        Optional<ProductEntity> optionalProduct = productRepository.findById(productId);
//
//        ProductEntity product = optionalProduct.get();
//        product.setPrice(2000L);
//
//        ProductEntity updatedProduct = productRepository.save(product);
//        assertThat(updatedProduct.getPrice()).isEqualTo(2000L);
//
//    }
//
//    @Test
//    @Rollback(false)
//    @DisplayName("상품 삭제 테스트")
//    void deleteProductTest() {
//        Long productId = 1L; // Assumes a product with this ID exists
//        boolean existsBeforeDelete = productRepository.findById(productId).isPresent();
//
//        productRepository.deleteById(productId);
//
//        boolean notExistsAfterDelete = productRepository.findById(productId).isEmpty();
//
//        assertThat(existsBeforeDelete).isTrue();
//        assertThat(notExistsAfterDelete).isTrue();
//    }
//
//    @Test
//    @DisplayName("상품 이름 길이 제한 테스트")
//    void prodcutNameLengthTest() {
//        ProductEntity product = new ProductEntity();
//        product.setName("AAAAAAAAAAAAAAAAAAAAAAA"); // 15글자 초과
//        product.setPrice(1000L);
//        product.setImageUrl("http://example.com/image.jpg");
//
//        assertThatThrownBy(() -> {
//            productRepository.save(product);
//            productRepository.flush(); // 즉시 데이터베이스에 반영하여 검증 오류를 트리거
//        }).isInstanceOf(DataIntegrityViolationException.class);
//    }
//}
