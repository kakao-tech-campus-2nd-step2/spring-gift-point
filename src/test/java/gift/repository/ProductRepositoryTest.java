package gift.repository;

import gift.entity.CategoryEntity;
import gift.entity.ProductEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveProduct() {
        // given
        ProductEntity productEntity = new ProductEntity("아이스 아메리카노", 1000, "http://test.com/image.jpg",new CategoryEntity());

        // when
        ProductEntity savedProduct = productRepository.save(productEntity);

        // then
        assertNotNull(savedProduct.getId());
        assertEquals(productEntity.getName(), savedProduct.getName());
    }

    @Test
    void testFindProductById() {
        // given
        ProductEntity productEntity = new ProductEntity("아이스 아메리카노", 1000, "http://test.com/image.jpg",new CategoryEntity());
        ProductEntity savedProduct = productRepository.save(productEntity);

        // when
        ProductEntity foundProduct = productRepository.findById(savedProduct.getId()).orElse(null);

        // then
        assertNotNull(foundProduct);
        assertEquals(savedProduct.getId(), foundProduct.getId());
    }

    @Test
    void testDeleteProduct() {
        // given
        ProductEntity productEntity = new ProductEntity("아이스 아메리카노", 1000, "http://test.com/image.jpg",new CategoryEntity());
        ProductEntity savedProduct = productRepository.save(productEntity);

        // when
        productRepository.deleteById(savedProduct.getId());
        ProductEntity foundProduct = productRepository.findById(savedProduct.getId()).orElse(null);

        // then
        assertNull(foundProduct);
    }
}
