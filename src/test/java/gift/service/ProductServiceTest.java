package gift.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gift.dto.ProductDTO;
import gift.model.Category;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional(readOnly = true)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product product1;
    private Product product2;
    private Category category;
    private Category savedCategory;
    private Pageable pageable = PageRequest.of(0, 5);

    @BeforeEach
    void setUp() {
        category = new Category(1L, "교환권");
        savedCategory = categoryRepository.save(category);
        product1 = new Product(1L, "상품", "100", savedCategory, "https://kakao");
        product2 = new Product(2L, "상품2", "200", savedCategory, "https://kakao2");
        productRepository.save(product1);
        productRepository.save(product2);
    }

    @Test
    void testFindAllProducts() {
        Page<Product> products = productService.findAllProducts(pageable);
        assertEquals(2, products.getTotalElements());
    }

    @Test
    void testFindProductsById() {
        Long productId = product1.getId();
        Product product = productService.findProductsById(productId);
        assertAll(
            () -> assertNotNull(product),
            () -> assertEquals(productId, product.getId())
        );
    }

    @Test
    @Transactional
    void testSaveProduct() {
        ProductDTO productDTO = new ProductDTO("상품3", "100", category.getId(), "https://kakao");
        productService.saveProduct(productDTO);
        List<Product> savedProducts = productRepository.findAll();
        assertAll(
            () -> assertEquals(3, savedProducts.size()),
            () -> assertEquals("상품3", savedProducts.get(2).getName())
        );
    }

    @Test
    @Transactional
    void testUpdateProduct() {
        Long productId = product1.getId();
        ProductDTO productDTO = new ProductDTO("상품3", "100", category.getId(), "https://kakao");
        productService.updateProduct(productDTO, productId);

        Product updatedProduct = productRepository.findById(productId).get();
        assertAll(
            () -> assertNotNull(updatedProduct),
            () -> assertEquals(productId, updatedProduct.getId()),
            () -> assertEquals("상품3", updatedProduct.getName()),
            () -> assertEquals("100", updatedProduct.getPrice()),
            () -> assertEquals("https://kakao", updatedProduct.getImageUrl())
        );
    }

    @Test
    @Transactional
    void testDeleteProductAndWishlist() {
        Long productId = product1.getId();
        productService.deleteProductAndWishlistAndOptions(productId);
        boolean exists = productRepository.existsById(productId);
        assertThat(exists).isFalse();
    }

}
