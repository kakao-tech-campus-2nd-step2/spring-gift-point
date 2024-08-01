package gift.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import gift.dto.CategoryDTO;
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

    private Category category;
    private Category testCategory;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testCategory = new Category(null, "테스트");
        testProduct = new Product(null, "상품1", "100", testCategory, "https://kakao");
        categoryRepository.save(testCategory);
        productRepository.save(testProduct);
    }

    @Test
    void testFindAllCategories() {
        List<Category> categories = categoryService.findAllCategories();
        assertAll(
            () -> assertFalse(categories.isEmpty()),
            () -> assertEquals("테스트", categories.get(2).getName())
        );
    }

    @Test
    void testFindCategoryById() {
        category = categoryService.findCategoryById(testCategory.getId());
        assertAll(
            () -> assertNotNull(category),
            () -> assertEquals("테스트", category.getName())
        );
    }

    @Test
    void testFindCategoryByName() {
        category = categoryService.findCategoryByName(testCategory.getName());
        assertAll(
            () -> assertNotNull(category),
            () -> assertEquals("테스트", category.getName())
        );
    }

    @Test
    @Transactional
    void testSaveCategory() {
        CategoryDTO newCategoryDTO = new CategoryDTO("new테스트");
        categoryService.saveCategory(newCategoryDTO);
        Category savedCategory = categoryRepository.findByName("new테스트");
        assertAll(
            () -> assertNotNull(savedCategory),
            () -> assertEquals("new테스트", savedCategory.getName())
        );
    }

    @Test
    @Transactional
    void testUpdateCategory() {
        CategoryDTO updatedCategoryDTO = new CategoryDTO("update테스트");
        categoryService.updateCategory(updatedCategoryDTO, testCategory.getId());

        Category updatedCategory = categoryRepository.findById(testCategory.getId()).get();
        assertAll(
            () -> assertNotNull(updatedCategory),
            () -> assertEquals("update테스트", updatedCategory.getName())
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