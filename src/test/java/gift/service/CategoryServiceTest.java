package gift.service;

import gift.domain.CategoryDTO;
import gift.entity.Category;
import gift.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.persistence.PersistenceException;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Given
        List<Category> categories = Arrays.asList(
                new Category("Category1", "Red", "image1.jpg", "Description1"),
                new Category("Category2", "Blue", "image2.jpg", "Description2")
        );
        when(categoryRepository.findAll()).thenReturn(categories);

        // When
        List<Category> result = categoryService.findAll();

        // Then
        assertEquals(2, result.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void testFindAllThrowsPersistenceException() {
        // Given
        when(categoryRepository.findAll()).thenThrow(PersistenceException.class);

        // When & Then
        assertThrows(PersistenceException.class, () -> categoryService.findAll());
    }

    @Test
    void testFindById() {
        // Given
        int id = 1;
        Category category = new Category("Category1", "Red", "image1.jpg", "Description1");
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        // When
        Optional<Category> result = categoryService.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(category, result.get());
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        int id = 1;
        when(categoryRepository.findById(id)).thenReturn(null);

        // When
        Optional<Category> result = categoryService.findById(id);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void testAdd() {
        // Given
        CategoryDTO categoryDTO = new CategoryDTO("NewCategory", "Green", "newimage.jpg", "NewDescription");
        Category category = new Category("NewCategory", "Green", "newimage.jpg", "NewDescription");
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // When
        Category result = categoryService.add(categoryDTO);

        // Then
        assertNotNull(result);
        assertEquals("NewCategory", result.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testAddThrowsPersistenceException() {
        // Given
        CategoryDTO categoryDTO = new CategoryDTO("NewCategory", "Green", "newimage.jpg", "NewDescription");
        when(categoryRepository.save(any(Category.class))).thenThrow(PersistenceException.class);

        // When & Then
        assertThrows(PersistenceException.class, () -> categoryService.add(categoryDTO));
    }

    @Test
    void testUpdate() {
        // Given
        int id = 1;
        CategoryDTO categoryDTO = new CategoryDTO("UpdatedCategory", "Yellow", "updatedimage.jpg", "UpdatedDescription");
        Category updatedCategory = new Category(id, "UpdatedCategory", "Yellow", "updatedimage.jpg", "UpdatedDescription");
        when(categoryRepository.findById(id)).thenReturn(Optional.of(updatedCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        // When
        Optional<Category> result = categoryService.update(id, categoryDTO);

        // Then
        assertTrue(result.isPresent());
        assertEquals("UpdatedCategory", result.get().getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testUpdateNotFound() {
        // Given
        int id = 1;
        CategoryDTO categoryDTO = new CategoryDTO("UpdatedCategory", "Yellow", "updatedimage.jpg", "UpdatedDescription");
        when(categoryRepository.findById(id)).thenReturn(null);

        // When
        Optional<Category> result = categoryService.update(id, categoryDTO);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void testDelete() {
        // Given
        int id = 1;
        doNothing().when(categoryRepository).deleteById(id);

        // When
        assertDoesNotThrow(() -> categoryService.delete(id));

        // Then
        verify(categoryRepository).deleteById(id);
    }

    @Test
    void testDeleteThrowsNoSuchElementException() {
        // Given
        int id = 1;
        doThrow(NoSuchElementException.class).when(categoryRepository).deleteById(id);

        // When & Then
        assertThrows(NoSuchElementException.class, () -> categoryService.delete(id));
    }

    @Test
    void testDeleteThrowsPersistenceException() {
        // Given
        int id = 1;
        doThrow(PersistenceException.class).when(categoryRepository).deleteById(id);

        // When & Then
        assertThrows(PersistenceException.class, () -> categoryService.delete(id));
    }
}