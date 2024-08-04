package gift.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import gift.dto.categoryDTO.CategoryRequestDTO;
import gift.dto.categoryDTO.CategoryResponseDTO;
import gift.model.Category;
import gift.model.Product;
import gift.model.Wishlist;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional(readOnly = true)
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    private CategoryResponseDTO category;
    private Category testCategory;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testCategory = new Category(null, "테스트", "#770077", "테스트 이미지", "테스트 설명");
        testProduct = new Product(null, "상품1", "100", testCategory, "https://kakao");
        categoryRepository.save(testCategory);
        productRepository.save(testProduct);
    }

    @Test
    void testGetAllCategories() {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        assertAll(
            () -> assertFalse(categories.isEmpty()),
            () -> assertEquals("테스트", categories.get(2).name())
        );
    }

    @Test
    void testGetCategoryById() {
        category = categoryService.getCategoryById(testCategory.getId());
        assertAll(
            () -> assertNotNull(category),
            () -> assertEquals("테스트", category.name())
        );
    }

    @Test
    void testGetCategoryByName() {
        category = categoryService.getCategoryByName(testCategory.getName());
        assertAll(
            () -> assertNotNull(category),
            () -> assertEquals("테스트", category.name())
        );
    }

    @Test
    @Transactional
    void testAddCategory() {
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO("new테스트", "#770077",
            "테스트 이미지2", "테스트 설명2");
        categoryService.addCategory(categoryRequestDTO);
        Category savedCategory = categoryRepository.findByName("new테스트");
        assertAll(
            () -> assertNotNull(savedCategory),
            () -> assertEquals("new테스트", savedCategory.getName()),
            () -> assertEquals("#770077", savedCategory.getColor()),
            () -> assertEquals("테스트 이미지2", savedCategory.getImageUrl()),
            () -> assertEquals("테스트 설명2", savedCategory.getDescription())
        );
    }

    @Test
    @Transactional
    void testUpdateCategory() {
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO("update테스트", "#770077",
            "테스트 이미지2", "테스트 설명2");
        categoryService.updateCategory(testCategory.getId(), categoryRequestDTO);

        Category updatedCategory = categoryRepository.findById(testCategory.getId()).get();
        assertAll(
            () -> assertNotNull(updatedCategory),
            () -> assertEquals("update테스트", updatedCategory.getName()),
            () -> assertEquals("#770077", updatedCategory.getColor()),
            () -> assertEquals("테스트 이미지2", updatedCategory.getImageUrl()),
            () -> assertEquals("테스트 설명2", updatedCategory.getDescription())
        );
    }

    @Test
    @Transactional
    void testDeleteCategory() {
        categoryService.deleteCategory(testCategory.getId());
        Category deletedCategory = categoryRepository.findById(testCategory.getId()).orElse(null);
        List<Product> remainingProducts = productRepository.findAll();
        List<Wishlist> remainingWishlists = wishlistRepository.findAll();
        assertAll(
            () -> assertNull(deletedCategory),
            () -> assertEquals(2, remainingProducts.size()),
            () -> assertEquals(0, remainingWishlists.size())
        );

    }
}