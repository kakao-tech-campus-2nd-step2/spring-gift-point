package gift.service;

import static org.assertj.core.api.Assertions.assertThat;

import gift.dto.CategoryRequest;
import gift.dto.CategoryResponse;
import gift.entity.Category;
import gift.entity.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.validator.ProductNameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@DataJpaTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private ProductService productService;
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
        productRepository.deleteAll();

        categoryService = new CategoryService(categoryRepository);
        productService = new ProductService(productRepository, new ProductNameValidator(),
            categoryService);

        CategoryRequest categoryRequest = new CategoryRequest("test카테고리", "#FFFFFF",
            "http://example.com/category1.jpg", "");
        CategoryResponse categoryResponse = categoryService.addCategory(categoryRequest);
        category = categoryRepository.findById(categoryResponse.getId()).orElseThrow();

        Product product1 = new Product("Product 1", 100, "http://example.com/product1.jpg",
            category);
        Product product2 = new Product("Product 2", 200, "http://example.com/product2.jpg",
            category);
        Product product3 = new Product("Product 3", 300, "http://example.com/product3.jpg",
            category);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
    }

    @Test
    @DisplayName("페이징된 상품 목록 가져오기")
    void testGetPagedProducts() {
        Pageable pageable = PageRequest.of(0, 2);
        Slice<Product> productPage = productService.findAll(pageable);

        assertThat(productPage).isNotNull();
        assertThat(productPage.getContent()).hasSize(2);
        assertThat(productPage.getContent().get(0).getName()).isEqualTo("Product 1");
        assertThat(productPage.getContent().get(1).getName()).isEqualTo("Product 2");
    }

}