package gift.test.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import gift.controller.CategoryController;
import gift.dto.CategoryRequest;
import gift.dto.CategoryResponse;
import gift.service.CategoryService;

public class CategoryTest {
    
    @Mock
    private CategoryService categoryService;
    
    @InjectMocks
    private CategoryController categoryController;
    
    @Mock
    private BindingResult bindingResult;
    
    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryRequest = new CategoryRequest("교환권", "#6c95d1", "https://example.com/image.jpg", "");
        categoryResponse = new CategoryResponse(1L, "교환권", "#6c95d1", "https://example.com/image.jpg", "");
    }
    
    @Test
    public void testGetCategories() {
        List<CategoryResponse> responses = Collections.singletonList(categoryResponse);
        
        when(categoryService.getAllCategories()).thenReturn(responses);
        ResponseEntity<List<CategoryResponse>> response = categoryController.getAllCategories();
        
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(responses);
    }
    
    @Test
    public void testAddCategory() {
        doNothing().when(categoryService).createCategory(any(CategoryRequest.class), any(BindingResult.class));
        ResponseEntity<Void> response = categoryController.addCategory(categoryRequest, bindingResult);
        
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
    }
    
    @Test
    public void testUpdateCategory() {
        doNothing().when(categoryService).updateCategory(eq(1L), any(CategoryRequest.class), any(BindingResult.class));
        
        ResponseEntity<Void> response = categoryController.updateCategory(1L, categoryRequest, bindingResult);
    
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
}
