package gift.doamin.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import gift.doamin.category.dto.CategoryForm;
import gift.doamin.category.dto.CategoryParam;
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
        categoryService.createCategory(new CategoryForm("test"));

        then(categoryRepository).should().save(any());
    }

    @Test
    void getAllCategories() {
        given(categoryRepository.findAll())
            .willReturn(List.of(new Category("test1"), new Category("test2")));

        List<CategoryParam> categories = categoryService.getAllCategories();

        assertThat(categories.size()).isEqualTo(2);
    }

    @Test
    void getCategory_existing() {
        given(categoryRepository.findById(1L))
            .willReturn(Optional.of(new Category("test")));

        CategoryParam category = categoryService.getCategory(1L);

        assertThat(category.getName()).isEqualTo("test");
    }

    @Test
    void getCategory_nonExistent() {
        given(categoryRepository.findById(2L))
            .willReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategory(2L))
            .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void updateCategory_existing() {
        given(categoryRepository.findById(1L))
            .willReturn(Optional.of(new Category("test")));
        CategoryForm categoryForm = new CategoryForm("test2");
        categoryForm.setId(1L);

        categoryService.updateCategory(categoryForm);

        then(categoryRepository).should().save(any());
    }

    @Test
    void updateCategory_nonExistent() {
        given(categoryRepository.findById(2L))
            .willReturn(Optional.empty());
        CategoryForm categoryForm = new CategoryForm("test2");
        categoryForm.setId(2L);

        assertThatThrownBy(() -> categoryService.updateCategory(categoryForm))
            .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void deleteCategory() {
        categoryService.deleteCategory(1L);

        then(categoryRepository).should().deleteById(any());
    }

}