package gift;

import gift.model.Category;
import gift.repository.CategoryRepository;
import gift.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCategories() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));

        List<Category> categories = categoryService.getAllCategories();
        assertEquals(1, categories.size());
        assertEquals("Test Category", categories.get(0).getName());

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.findById(1L);
        assertEquals("Test Category", foundCategory.getName());

        verify(categoryRepository, times(1)).findById(1L);
    }


}