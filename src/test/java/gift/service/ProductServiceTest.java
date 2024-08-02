package gift.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import gift.DTO.product.ProductPageResponse;
import gift.DTO.product.ProductResponse;
import gift.domain.Category;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testProductPaginationWithNoCategory() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductResponse> response =
            productService.findPagedProductsByCategoryId(pageable, null);

        assertFalse(response.getContent().isEmpty());
    }

    @Test
    public void testProductPaginationWithCategory() {
        Category category = categoryRepository.findById(1L).orElseThrow(
            () -> new RuntimeException("Category not found"));
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductResponse> response =
            productService.findPagedProductsByCategoryId(pageable, category.getId());

        assertFalse(response.getContent().isEmpty());
        assertEquals(category.getId(), response.getContent().get(0).getCategoryId());
    }
}
