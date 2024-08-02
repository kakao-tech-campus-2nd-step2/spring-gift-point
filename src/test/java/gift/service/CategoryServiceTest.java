package gift.service;

import gift.category.dto.CategoryRequestDto;
import gift.category.dto.CategoryResponseDto;
import gift.category.entity.Category;
import gift.category.repository.CategoryRepository;
import gift.category.service.CategoryService;
import gift.exception.DuplicateResourceException;
import gift.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CategoryServiceTest {

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CategoryService categoryService;

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetAllCategoriesWhenNoCategoriesExist() {
    when(categoryRepository.findAll()).thenReturn(List.of());

    ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
      categoryService.getAllCategories();
    });

    assertEquals("등록된 카테고리가 존재하지 않습니다.", thrown.getMessage());
  }

  @Test
  public void testGetAllCategoriesWhenCategoriesExist() {
    Category category = new Category("Category1", "Red", "imageUrl", "Description");
    when(categoryRepository.findAll()).thenReturn(List.of(category));

    List<CategoryResponseDto> result = categoryService.getAllCategories();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Category1", result.get(0).name());
  }

  @Test
  public void testCreateCategoryWhenCategoryDoesNotExist() {
    CategoryRequestDto requestDto = new CategoryRequestDto("Category1", "Red", "imageUrl",
        "Description");
    when(categoryRepository.existsByName("Category1")).thenReturn(false);
    when(categoryRepository.save(any(Category.class))).thenAnswer(
        invocation -> invocation.getArguments()[0]);

    CategoryResponseDto result = categoryService.createCategory(requestDto);

    assertNotNull(result);
    assertEquals("Category1", result.name());
    verify(categoryRepository, times(1)).save(any(Category.class));
  }

  @Test
  public void testCreateCategoryWhenCategoryAlreadyExists() {
    CategoryRequestDto requestDto = new CategoryRequestDto("Category1", "Red", "imageUrl",
        "Description");
    when(categoryRepository.existsByName("Category1")).thenReturn(true);

    DuplicateResourceException thrown = assertThrows(DuplicateResourceException.class, () -> {
      categoryService.createCategory(requestDto);
    });

    assertEquals("이미 존재하는 카테고리 이름입니다.", thrown.getMessage());
  }

  @Test
  public void testUpdateCategoryWhenCategoryExists() {
    Long categoryId = 1L;
    CategoryRequestDto requestDto = new CategoryRequestDto("UpdatedCategory", "Blue", "newImageUrl",
        "New Description");
    Category category = new Category("Category1", "Red", "imageUrl", "Description");
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
    when(categoryRepository.existsByName("UpdatedCategory")).thenReturn(false);

    CategoryResponseDto result = categoryService.updateCategory(categoryId, requestDto);

    assertNotNull(result);
    assertEquals("UpdatedCategory", result.name());
    verify(categoryRepository, times(1)).findById(anyLong());
  }

  @Test
  public void testUpdateCategoryWhenCategoryDoesNotExist() {
    Long categoryId = 1L;
    CategoryRequestDto requestDto = new CategoryRequestDto("UpdatedCategory", "Blue", "newImageUrl",
        "New Description");
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

    ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
      categoryService.updateCategory(categoryId, requestDto);
    });

    assertEquals("해당 카테고리를 찾을 수 없습니다.", thrown.getMessage());
  }

  @Test
  public void testUpdateCategoryWhenCategoryNameAlreadyExists() {
    Long categoryId = 1L;
    CategoryRequestDto requestDto = new CategoryRequestDto("ExistingCategory", "Blue",
        "newImageUrl", "New Description");
    Category category = new Category("Category1", "Red", "imageUrl", "Description");
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
    when(categoryRepository.existsByName("ExistingCategory")).thenReturn(true);

    DuplicateResourceException thrown = assertThrows(DuplicateResourceException.class, () -> {
      categoryService.updateCategory(categoryId, requestDto);
    });

    assertEquals("이미 존재하는 카테고리 이름입니다.", thrown.getMessage());
  }

  @Test
  public void testGetCategoryWhenCategoryExists() {
    Long categoryId = 1L;
    Category category = new Category("Category1", "Red", "imageUrl", "Description");
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

    CategoryResponseDto result = categoryService.getCategory(categoryId);

    assertNotNull(result);
    assertEquals("Category1", result.name());
  }

  @Test
  public void testGetCategoryWhenCategoryDoesNotExist() {
    Long categoryId = 1L;
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

    ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
      categoryService.getCategory(categoryId);
    });

    assertEquals("해당 카테고리를 찾을 수 없습니다.", thrown.getMessage());
  }

  @Test
  public void testDeleteCategoryWhenCategoryExists() {
    Long categoryId = 1L;
    Category category = new Category("Category1", "Red", "imageUrl", "Description");
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

    categoryService.deleteCategory(categoryId);

    verify(categoryRepository, times(1)).delete(category);
  }

  @Test
  public void testDeleteCategoryWhenCategoryDoesNotExist() {
    Long categoryId = 1L;
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

    ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
      categoryService.deleteCategory(categoryId);
    });

    assertEquals("해당 카테고리를 찾을 수 없습니다.", thrown.getMessage());
  }

  @Test
  void cors() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.options("/api/products")
            .header(HttpHeaders.ORIGIN, "http://localhost:8080")
            .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
        .andExpect(status().isOk())
        .andExpect(
            header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*"))
        .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
            "GET,POST,PUT,DELETE,OPTIONS"))
        .andDo(print());
  }


}