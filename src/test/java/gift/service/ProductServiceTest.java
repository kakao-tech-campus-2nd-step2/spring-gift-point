package gift.service;

import gift.dto.CategoryRequestDto;
import gift.dto.ProductPageResponseDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.entity.ProductName;
import gift.exception.BusinessException;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @Rollback
    public void 상품_추가_성공() {
        Long category = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리")).getId();
        ProductRequestDto requestDTO = new ProductRequestDto("아이스 카페 아메리카노 T", 4500, "https://example.com/product1.jpg", category);
        ProductResponseDto createdProduct = productService.addProduct(requestDTO);

        assertNotNull(createdProduct);
        assertNotNull(createdProduct.getId());
        assertEquals("아이스 카페 아메리카노 T", createdProduct.getName());
        assertEquals(4500, createdProduct.getPrice());
        assertEquals("https://example.com/product1.jpg", createdProduct.getImageUrl());
        assertEquals("테스트카테고리", createdProduct.getCategory().getName());
    }

    @Test
    @Rollback
    public void 상품_조회_성공() {
        Long category = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리")).getId();
        Product product = new Product(new ProductName("오둥이 입니다만"), 29800, "https://example.com/product2.jpg", categoryService.getCategoryEntityById(category));
        productRepository.save(product);

        ProductPageResponseDto productPage = productService.getAllProducts(0, 10);

        assertNotNull(productPage);
        assertEquals(1, productPage.getTotalItems());
        ProductResponseDto retrievedProduct = productPage.getProducts().get(0);
        assertEquals("오둥이 입니다만", retrievedProduct.getName());
        assertEquals(29800, retrievedProduct.getPrice());
        assertEquals("https://example.com/product2.jpg", retrievedProduct.getImageUrl());
        assertEquals("테스트카테고리", retrievedProduct.getCategory().getName());
    }

    @Test
    @Rollback
    public void 상품_수정_성공() {
        Long category = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리")).getId();
        Product originalProduct = new Product(new ProductName("오둥이 입니다만"), 29800, "https://example.com/product2.jpg", categoryService.getCategoryEntityById(category));
        productRepository.save(originalProduct);

        ProductRequestDto updateDTO = new ProductRequestDto("오둥이 아닙니다만", 35000, "https://example.com/product3.jpg", category);
        ProductResponseDto result = productService.updateProduct(originalProduct.getId(), updateDTO);

        assertNotNull(result);
        assertEquals(originalProduct.getId(), result.getId());
        assertEquals("오둥이 아닙니다만", result.getName());
        assertEquals(35000, result.getPrice());
        assertEquals("https://example.com/product3.jpg", result.getImageUrl());
        assertEquals("테스트카테고리", result.getCategory().getName());
    }

    @Test
    @Rollback
    public void 상품_수정_없는상품_예외_발생() {
        Long category = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리")).getId();
        ProductRequestDto updateDTO = new ProductRequestDto("오둥이 아닙니다만", 35000, "https://example.com/product3.jpg", category);

        assertThrows(BusinessException.class, () -> productService.updateProduct(100L, updateDTO));
    }

    @Test
    @Rollback
    public void 상품_삭제_성공() {
        Long category = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리")).getId();
        Product product = new Product(new ProductName("오둥이 입니다만"), 29800, "https://example.com/product2.jpg", categoryService.getCategoryEntityById(category));
        productRepository.save(product);

        productService.deleteProduct(product.getId());

        ProductPageResponseDto productPage = productService.getAllProducts(0, 10);
        assertTrue(productPage.getProducts().isEmpty());
    }

    @Test
    @Rollback
    public void 상품_삭제_없는상품_예외_발생() {
        assertThrows(BusinessException.class, () -> productService.deleteProduct(2L));
    }

    @Rollback
    @TestFactory
    public Stream<DynamicTest> 상품_목록_페이지네이션_성공() {
        Long category = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리")).getId();
        productRepository.save(new Product(new ProductName("상품 1"), 1000, "https://example.com/product1.jpg", categoryService.getCategoryEntityById(category)));
        productRepository.save(new Product(new ProductName("상품 2"), 2000, "https://example.com/product2.jpg", categoryService.getCategoryEntityById(category)));
        productRepository.save(new Product(new ProductName("상품 3"), 3000, "https://example.com/product3.jpg", categoryService.getCategoryEntityById(category)));
        productRepository.save(new Product(new ProductName("상품 4"), 4000, "https://example.com/product4.jpg", categoryService.getCategoryEntityById(category)));
        productRepository.save(new Product(new ProductName("상품 5"), 5000, "https://example.com/product5.jpg", categoryService.getCategoryEntityById(category)));

        return Stream.of(DynamicTest.dynamicTest("첫번째 페이지 조회", () -> {
            ProductPageResponseDto page1 = productService.getAllProducts(0, 2);
            assertNotNull(page1);
            assertEquals(2, page1.getProducts().size());
            assertEquals(0, page1.getCurrentPage());
            assertEquals(3, page1.getTotalPages());
        }), DynamicTest.dynamicTest("두번째 페이지 조회", () -> {
            ProductPageResponseDto page2 = productService.getAllProducts(1, 2);
            assertNotNull(page2);
            assertEquals(2, page2.getProducts().size());
            assertEquals(1, page2.getCurrentPage());
        }), DynamicTest.dynamicTest("세번째 페이지 조회", () -> {
            ProductPageResponseDto page3 = productService.getAllProducts(2, 2);
            assertNotNull(page3);
            assertEquals(1, page3.getProducts().size());
            assertEquals(2, page3.getCurrentPage());
        }));
    }
}
