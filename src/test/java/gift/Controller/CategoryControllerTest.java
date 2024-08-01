package gift.Controller;

import gift.Service.CategoryService;
import gift.DTO.CategoryDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    public void testCreateCategory() {
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Test Category");
        when(categoryService.createCategory(categoryDTO)).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> response = categoryController.createCategory(categoryDTO);

        assertEquals(ResponseEntity.ok(categoryDTO), response);
        verify(categoryService, times(1)).createCategory(categoryDTO);
    }

    @Test
    public void testGetCategoryById() {
        Long id = 1L;
        CategoryDTO categoryDTO = new CategoryDTO(id, "Test Category");
        when(categoryService.findById(id)).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> response = categoryController.getCategoryById(id);

        assertEquals(ResponseEntity.ok(categoryDTO), response);
        verify(categoryService, times(1)).findById(id);
    }

    @Test
    public void testUpdateCategory() {
        Long id = 1L;
        CategoryDTO categoryDTO = new CategoryDTO(id, "Updated Category");
        when(categoryService.updateCategory(id, categoryDTO)).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> response = categoryController.updateCategory(id, categoryDTO);

        assertEquals(ResponseEntity.ok(categoryDTO), response);
        verify(categoryService, times(1)).updateCategory(id, categoryDTO);
    }

}
