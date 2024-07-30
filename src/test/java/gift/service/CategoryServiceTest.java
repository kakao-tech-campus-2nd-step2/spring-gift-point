package gift.service;//package gift.service;
//
//import gift.entity.Category;
//import gift.repository.CategoryRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class CategoryServiceTest {
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @InjectMocks
//    private CategoryService categoryService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testFindAll() {
//        // Given
//        Category category1 = new Category("Electronics", "Blue", "http://example.com/image1.jpg", "Description 1");
//        Category category2 = new Category("Books", "Red", "http://example.com/image2.jpg", "Description 2");
//        List<Category> mockCategories = Arrays.asList(category1, category2);
//
//        when(categoryRepository.findAll()).thenReturn(mockCategories);
//
//        // When
//        List<Category> categories = categoryService.findAll();
//
//        // Then
//        assertEquals(2, categories.size());
//        assertEquals("Electronics", categories.get(0).getName());
//        assertEquals("Books", categories.get(1).getName());
//        verify(categoryRepository, times(1)).findAll();
//    }
//}