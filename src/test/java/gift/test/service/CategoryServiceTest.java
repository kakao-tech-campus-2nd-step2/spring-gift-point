package gift.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import gift.dto.CategoryRequest;
import gift.dto.CategoryResponse;
import gift.entity.Category;
import gift.repository.CategoryRepository;
import gift.service.CategoryService;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    BindingResult bindingResult;

    private CategoryRequest categoryRequest;
    private Category category;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryRequest = new CategoryRequest("교환권", "#6c95d1", "https://example.com/image.jpg", "");
        category = new Category("교환권", "#6c95d1", "https://example.com/image.jpg", "");
        category.setId(1L);
    }

    @Test
    public void testGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));
        List<CategoryResponse> responses = categoryService.getAllCategories();
        
        verify(categoryRepository).findAll();
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0)).isEqualTo(category.toDto());
    }

    @Test
    public void testCreateCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        categoryService.createCategory(categoryRequest, bindingResult);
        
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    public void testUpdateCategory() {
        when(categoryRepository.existsById(any(Long.class))).thenReturn(true);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        categoryService.updateCategory(1L, categoryRequest, bindingResult);
        
        verify(categoryRepository).existsById(any(Long.class));
        verify(categoryRepository).save(any(Category.class));
    }
}
