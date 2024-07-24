package gift.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.category.model.CategoryRepository;
import gift.category.model.dto.Category;
import gift.category.model.dto.CategoryRequest;
import gift.category.model.dto.CategoryResponse;
import gift.category.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category defaultCategory;
    private CategoryRequest defaultRequest;

    @BeforeEach
    void setUp() {
        defaultCategory = new Category("기본", "기본 카테고리");
        defaultRequest = new CategoryRequest("수정/추가 카테고리", "새 카테고리입니다");
    }

    /* 테스트 네이밍 : 메소드명_기대결과_테스트상태 */
    @Test
    @DisplayName("카테고리가 존재할 때 categoryResponse를 반환한다.")
    void findCategoryById_shouldReturnCategoryResponse_whenCategoryExists() {
        // given
        when(categoryRepository.findById(defaultCategory.getId())).thenReturn(
                Optional.of(defaultCategory));

        // when
        CategoryResponse response = categoryService.findCategoryById(defaultCategory.getId());

        // then
        assertNotNull(response);
        assertEquals(defaultCategory.getName(), response.getName());
        verify(categoryRepository, times(1)).findById(defaultCategory.getId());
    }

    @Test
    @DisplayName("카테고리가 존재하지 않을 때 EntityNotFoundException을 던진다.")
    void findCategoryById_shouldThrowEntityNotFoundException_whenCategoryDoesNotExist() {
        // given
        Long missingId = 99L;
        when(categoryRepository.findById(missingId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(EntityNotFoundException.class, () -> categoryService.findCategoryById(missingId));
        verify(categoryRepository, times(1)).findById(missingId);
    }

    @Test
    @DisplayName("모든 카테고리를 반환한다. 카테고리가 없을 경우에도 오류를 던지지 않는다.")
    void findAllCategories_shouldReturnListOfCategoryResponses() {
        // given
        Category anotherCategory = new Category("2번 카테고리", "2번입니다");
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(defaultCategory, anotherCategory));

        // when
        List<CategoryResponse> responses = categoryService.findAllCategories();

        // then
        assertEquals(2, responses.size());
        assertEquals(defaultCategory.getName(), responses.get(0).getName());
        assertEquals(anotherCategory.getName(), responses.get(1).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("새 카테고리를 추가한다.")
    void addCategory_shouldSaveDB() {
        // given, when
        categoryService.addCategory(defaultRequest);

        // then
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("해당 Id의 카테고리가 존재할 경우 기존 카테고리를 수정한다.")
    void updateCategory_shouldUpdateExistingCategory() {
        // given
        when(categoryRepository.findById(defaultCategory.getId())).thenReturn(
                Optional.of(defaultCategory));

        // when
        categoryService.updateCategory(defaultCategory.getId(), defaultRequest);

        // then
        assertEquals(defaultRequest.name(), defaultCategory.getName());
        assertEquals(defaultRequest.description(), defaultCategory.getDescription());
        verify(categoryRepository, times(1)).save(defaultCategory);
    }

    @Test
    @DisplayName("해당 Id의 카테고리가 존재하지 않을 경우 EntityNotFoundException을 던진다.")
    void updateCategory_shouldThrowEntityNotFoundException_whenCategoryDoesNotExist() {
        // given
        Long missingId = 99L;
        when(categoryRepository.findById(missingId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(EntityNotFoundException.class, () -> categoryService.updateCategory(missingId, defaultRequest));
        verify(categoryRepository, times(1)).findById(missingId);
    }

    @Test
    @DisplayName("해당 Id의 카테고리를 삭제한다.")
    void deleteCategory_shouldMarkCategoryAsInactive() {
        // given
        when(categoryRepository.findById(defaultCategory.getId())).thenReturn(
                Optional.of(defaultCategory));

        // when
        categoryService.deleteCategory(defaultCategory.getId());

        // then
        verify(categoryRepository, times(1)).delete(defaultCategory);
    }

    @Test
    @DisplayName("해당 Id의 카테고리가 존재하지 않을 경우 EntityNotFoundException을 던진다.")
    void deleteCategory_shouldThrowEntityNotFoundException_whenCategoryDoesNotExist() {
        // given
        Long missingId = 99L;
        when(categoryRepository.findById(missingId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteCategory(missingId));
        verify(categoryRepository, times(1)).findById(missingId);
    }
}
