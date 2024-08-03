package gift.doamin.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import gift.doamin.category.dto.CategoryRequest;
import gift.doamin.category.dto.CategoryResponse;
import gift.doamin.category.entity.Category;
import gift.doamin.category.exception.CategoryNotFoundException;
import gift.doamin.category.repository.JpaCategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryServiceTest {

    private JpaCategoryRepository categoryRepository = mock(JpaCategoryRepository.class);
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void createCategory() {
        given(categoryRepository.existsByName(any())).willReturn(false);
        given(categoryRepository.save(any())).willReturn(
            new Category("test", "#000000", "", "테스트 카테고리"));
        categoryService.createCategory(new CategoryRequest("test", "#000000", "", "테스트 카테고리"));

        then(categoryRepository).should().save(any());
    }

    @Test
    void getAllCategories() {
        given(categoryRepository.findAll()).willReturn(
            List.of(new Category("test1", "#000000", "", "테스트 카테고리"),
                new Category("test2", "#000000", "", "테스트 카테고리")));

        List<CategoryResponse> categories = categoryService.getAllCategories();

        assertThat(categories.size()).isEqualTo(2);
    }

    @Test
    void getCategory_existing() {
        given(categoryRepository.findById(1L)).willReturn(
            Optional.of(new Category("test", "#000000", "", "테스트 카테고리")));

        CategoryResponse category = categoryService.getCategory(1L);

        assertThat(category.getName()).isEqualTo("test");
    }

    @Test
    void getCategory_nonExistent() {
        given(categoryRepository.findById(2L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategory(2L)).isInstanceOf(
            CategoryNotFoundException.class);
    }

    @Test
    void updateCategory_existing() {
        given(categoryRepository.findById(1L)).willReturn(
            Optional.of(new Category("test", "#000000", "", "테스트 카테고리")));
        CategoryRequest categoryRequest = new CategoryRequest("test2", "#000000", "", "테스트 카테고리");

        var categoryResponse = categoryService.updateCategory(1L, categoryRequest);

        assertThat(categoryResponse.getName()).isEqualTo("test2");
    }

    @Test
    void updateCategory_nonExistent() {
        given(categoryRepository.findById(2L)).willReturn(Optional.empty());
        CategoryRequest categoryRequest = new CategoryRequest("test2", "#000000", "", "테스트 카테고리");

        assertThatThrownBy(() -> categoryService.updateCategory(2L, categoryRequest)).isInstanceOf(
            CategoryNotFoundException.class);
    }

    @Test
    void deleteCategory() {
        categoryService.deleteCategory(1L);

        then(categoryRepository).should().deleteById(any());
    }

}