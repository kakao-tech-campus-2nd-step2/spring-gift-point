package gift.service;

import gift.domain.Category;
import gift.dto.request.CategoryRequest;
import gift.exception.CategoryNotFoundException;
import gift.exception.DuplicateCategoryNameException;
import gift.repository.category.CategorySpringDataJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategorySpringDataJpaRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category mockCategory1;
    private Category mockCategory2;

    @BeforeEach
    public void setUp() {
        mockCategory1 = mock(Category.class);
        mockCategory2 = mock(Category.class);
    }

    @Test
    public void testGetCategories() {
        when(mockCategory1.getName()).thenReturn("교환권");
        when(mockCategory2.getName()).thenReturn("상품권");

        List<Category> mockCategories = List.of(mockCategory1, mockCategory2);
        when(categoryRepository.findAll()).thenReturn(mockCategories);

        List<Category> categories = categoryService.getCategories();

        assertEquals(2, categories.size());
        assertEquals("교환권", categories.get(0).getName());
        assertEquals("상품권", categories.get(1).getName());

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testGetCategoryById() {
        Long categoryId = 1L;

        when(mockCategory1.getName()).thenReturn("교환권");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory1));

        Category category = categoryService.getCategory(categoryId);

        assertEquals("교환권", category.getName());

        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void testGetCategoryByIdNotFound() {
        Long categoryId = 11L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategory(categoryId));

        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void testCreateCategory() {
        CategoryRequest categoryRequest = new CategoryRequest("교환권", "초록색", "image.url", "description");
        when(categoryRepository.existsByName(categoryRequest.getName())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(mockCategory1);
        when(mockCategory1.getName()).thenReturn("교환권");

        Category createdCategory = categoryService.createCategory(categoryRequest);

        assertEquals("교환권", createdCategory.getName());

        verify(categoryRepository, times(1)).existsByName("교환권");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testCreateCategoryDuplicateName() {
        CategoryRequest categoryRequest = new CategoryRequest("교환권", "초록색", "image.url", "description");
        when(categoryRepository.existsByName(categoryRequest.getName())).thenReturn(true);

        assertThrows(DuplicateCategoryNameException.class, () -> categoryService.createCategory(categoryRequest));

        verify(categoryRepository, times(1)).existsByName("교환권");
        verify(categoryRepository, never()).save(any());
    }

    @Test
    public void testUpdateCategory() {
        Long categoryId = 1L;
        CategoryRequest categoryRequest = new CategoryRequest("뷰티", "초록색", "image.url", "description");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory1));
        when(categoryRepository.existsByName(categoryRequest.getName())).thenReturn(false);

        Category updatedCategory = mock(Category.class);
        when(updatedCategory.getName()).thenReturn("뷰티");
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        Category result = categoryService.updateCategory(categoryId, categoryRequest);

        assertEquals("뷰티", result.getName());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).existsByName("뷰티");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }



    @Test
    public void testUpdateCategoryNotFound() {
        Long categoryId = 11L;
        CategoryRequest categoryRequest = new CategoryRequest("뷰티", "초록색", "image.url", "description");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(categoryId, categoryRequest));

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).existsByName(any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    public void testUpdateCategoryDuplicateName() {
        Long categoryId = 1L;
        CategoryRequest categoryRequest = new CategoryRequest("상품권", "초록색", "image.url", "description");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory1));
        when(categoryRepository.existsByName(categoryRequest.getName())).thenReturn(true);

        assertThrows(DuplicateCategoryNameException.class, () -> categoryService.updateCategory(categoryId, categoryRequest));

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).existsByName("상품권");
        verify(categoryRepository, never()).save(any());
    }

    @Test
    public void testDeleteCategory() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory1));

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).delete(mockCategory1);
    }

    @Test
    public void testDeleteCategoryNotFound() {
        Long categoryId = 11L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(categoryId));

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).delete(any());
    }
}
