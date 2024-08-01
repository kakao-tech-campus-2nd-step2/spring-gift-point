package gift;

import gift.dto.CategoryDto;
import gift.model.Category;
import gift.repository.CategoryRepository;
import gift.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class CategoryTest {

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CategoryService categoryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSaveCategory() {
    CategoryDto categoryDto = new CategoryDto(null, "category 1", "#000000", "image", "test1");
    Category category = new Category("category 1", "#000000", "image", "test1");
    when(categoryRepository.save(any(Category.class))).thenReturn(category);

    CategoryDto savedCategoryDto = categoryService.saveCategory(categoryDto);

    assertNotNull(savedCategoryDto);
    assertEquals("category 1", savedCategoryDto.getName());
  }

  @Test
  void testFindAllCategories() {
    List<Category> categories = Arrays.asList(
            new Category("category 1", "#000000", "image", "test1"),
            new Category("category 2", "#FFFFFF", "image2", "test2")
    );
    when(categoryRepository.findAll()).thenReturn(categories);

    List<CategoryDto> categoryDtos = categoryService.findAllCategories();

    assertEquals(2, categoryDtos.size());
    assertEquals("category 1", categoryDtos.get(0).getName());
    assertEquals("category 2", categoryDtos.get(1).getName());
  }
}