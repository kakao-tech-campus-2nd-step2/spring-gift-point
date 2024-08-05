package gift.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gift.dto.optionDTO.OptionRequestDTO;
import gift.dto.pageDTO.ProductPageResponseDTO;
import gift.dto.productDTO.ProductAddRequestDTO;
import gift.dto.productDTO.ProductGetResponseDTO;
import gift.dto.productDTO.ProductUpdateRequestDTO;
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
        category = new Category(1L, "교환권", "#770077", "테스트 이미지", "테스트 설명");
        savedCategory = categoryRepository.save(category);
        product1 = new Product(1L, "상품", "100", savedCategory, "https://kakao");
        product2 = new Product(2L, "상품2", "200", savedCategory, "https://kakao2");
        productRepository.save(product1);
        productRepository.save(product2);
    }

    @Test
    void testGetAllProducts() {
        ProductPageResponseDTO products = productService.getAllProducts(pageable);
        assertEquals(2, products.products().getContent().size());
    }

    @Test
    void testFindProductsById() {
        Long productId = product1.getId();
        ProductGetResponseDTO product = productService.getProductById(productId);
        assertAll(
            () -> assertNotNull(product),
            () -> assertEquals(productId, product.id())
        );
    }

    @Test
    @Transactional
    void testAddProduct() {
        List<OptionRequestDTO> options = List.of(new OptionRequestDTO("옵션", 10L));
        ProductAddRequestDTO productDTO = new ProductAddRequestDTO("상품3", "100", "https://kakao",
            savedCategory.getId(), options);
        productService.addProduct(productDTO);
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
        ProductUpdateRequestDTO productDTO = new ProductUpdateRequestDTO("상품3", "100",
            "https://kakao", savedCategory.getId());
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
