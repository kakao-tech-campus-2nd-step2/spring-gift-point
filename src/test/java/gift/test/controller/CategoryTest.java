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
import gift.entity.Category;
import gift.service.CategoryService;

public class CategoryTest {
	
	@Mock
	private CategoryService categoryService;
	
	@InjectMocks
	private CategoryController categoryController;
	
	@Mock
	private BindingResult bindingResult;
	
	private Category category;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		category = new Category("교환권", "#6c95d1", "https://example.com/image.jpg", "");
	}
	
	@Test
	public void testGetCategories() {
		List<Category> categories = Collections.singletonList(category);
		
		when(categoryService.getAllCategories()).thenReturn(categories);
		ResponseEntity<List<Category>> response = categoryController.getAllCategories();
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody()).isEqualTo(categories);
	}
	
	@Test
	public void testAddCategory() {
		doNothing().when(categoryService).createCategory(any(Category.class), any(BindingResult.class));
		ResponseEntity<Void> response = categoryController.addCategory(category, bindingResult);
		
		assertThat(response.getStatusCodeValue()).isEqualTo(201);
	}
	
	@Test
	public void testUpdateCategory() {
		doNothing().when(categoryService).updateCategory(eq(1L), any(Category.class), any(BindingResult.class));
		
		ResponseEntity<Void> response = categoryController.updateCategory(1L, category, bindingResult);
	
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
}
