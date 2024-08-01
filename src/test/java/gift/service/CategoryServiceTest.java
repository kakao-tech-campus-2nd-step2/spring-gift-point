package gift.service;

import gift.dto.CategoryDTO;
import gift.entity.CategoryEntity;
import gift.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    void createCategory_Success() {
        // Given
        CategoryDTO categoryDTO = new CategoryDTO(null, "Test Category", "#FFFFFF", "http://example.com/image.jpg", "Test Description");

        // When
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);

        // Then
        assertNotNull(createdCategory.getId(), "저장된 카테고리 ID는 null이 아니어야 합니다.");
        assertEquals("Test Category", createdCategory.getName(), "카테고리 이름은 'Test Category'이어야 합니다.");
        assertEquals("#FFFFFF", createdCategory.getColor(), "카테고리 색상은 '#FFFFFF'이어야 합니다.");
    }

    @Test
    void getCategory_Success() {
        // Given
        CategoryEntity categoryEntity = new CategoryEntity(null, "Test Category", "#FFFFFF", "http://example.com/image.jpg", "Test Description");
        CategoryEntity savedEntity = categoryRepository.save(categoryEntity);
        Long categoryId = savedEntity.getId();

        // When
        CategoryDTO foundCategory = categoryService.getCategory(categoryId);

        // Then
        assertNotNull(foundCategory, "찾은 카테고리는 null이 아니어야 합니다.");
        assertEquals("Test Category", foundCategory.getName(), "카테고리 이름은 'Test Category'이어야 합니다.");
        assertEquals("#FFFFFF", foundCategory.getColor(), "카테고리 색상은 '#FFFFFF'이어야 합니다.");
    }

    @Test
    void getCategory_NotFound() {
        // Given
        Long categoryId = 1L;

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.getCategory(categoryId);
        });
        assertEquals("카테고리를 찾을 수 없습니다", exception.getMessage());
    }

    @Test
    void updateCategory_Success() {
        // Given
        CategoryEntity categoryEntity = new CategoryEntity(null, "Test Category", "#FFFFFF", "http://example.com/image.jpg", "Test Description");
        CategoryEntity savedEntity = categoryRepository.save(categoryEntity);
        Long categoryId = savedEntity.getId();

        CategoryDTO categoryDTO = new CategoryDTO(categoryId, "Updated Category", "#000000", "http://example.com/updated.jpg", "Updated Description");

        // When
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);

        // Then
        assertEquals("Updated Category", updatedCategory.getName(), "업데이트된 카테고리 이름은 불일치");
        assertEquals("#000000", updatedCategory.getColor(), "업데이트된 카테고리 색상은 불일치");
    }

    @Test
    void updateCategory_NotFound() {
        // Given
        Long categoryId = 1L;
        CategoryDTO categoryDTO = new CategoryDTO(categoryId, "Updated Category", "#000000", "http://example.com/updated.jpg", "Updated Description");

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.updateCategory(categoryId, categoryDTO);
        });
        assertEquals("카테고리를 찾을 수 없습니다", exception.getMessage());
    }

    @Test
    void deleteCategory_Success() {
        // Given
        CategoryEntity categoryEntity = new CategoryEntity(null, "Test Category", "#FFFFFF", "http://example.com/image.jpg", "Test Description");
        CategoryEntity savedEntity = categoryRepository.save(categoryEntity);
        Long categoryId = savedEntity.getId();

        // When / Then
        assertDoesNotThrow(() -> {
            categoryService.deleteCategory(categoryId);
        });
    }

    @Test
    void deleteCategory_NotFound() {
        // Given
        Long categoryId = 1L;

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.deleteCategory(categoryId);
        });
        assertEquals("카테고리를 찾을 수 없습니다", exception.getMessage());
    }
}