package gift.category;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import gift.product.business.dto.CategoryRegisterDto;
import gift.product.business.dto.CategoryUpdateDto;
import gift.product.business.service.CategoryService;
import gift.product.persistence.entity.Category;
import gift.product.persistence.repository.CategoryRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    private static Category category;

    @BeforeAll
    static void setCategory() {
        category = mock(Category.class);
        doReturn(1L).when(category).getId();
        doReturn("name").when(category).getName();
    }

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void testGetCategories() {
        // given
        given(categoryRepository.getAllCategories())
            .willReturn(List.of(category));

        // when
        var categoryDtos = categoryService.getCategories();

        // then
        then(categoryRepository).should().getAllCategories();
        assertAll(
            () -> assertThat(categoryDtos.getFirst().id()).isEqualTo(1L),
            () -> assertThat(categoryDtos.getFirst().name()).isEqualTo("name")
        );
    }

    @Test
    void testCreateCategory() {
        // given
        var categoryRegisterDto = new CategoryRegisterDto("name");

        given(categoryRepository.saveCategory(any()))
            .willReturn(1L);

        // when
        var id = categoryService.createCategory(categoryRegisterDto);

        // then
        then(categoryRepository).should().saveCategory(any());
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void testUpdateCategory() {
        // given
        var categoryUpdateDto = new CategoryUpdateDto(1L, "new name");

        given(categoryRepository.getCategory(1L))
            .willReturn(category);
        given(categoryRepository.saveCategory(category))
            .willReturn(1L);

        // when
        var id = categoryService.updateCategory(categoryUpdateDto);

        // then
        then(categoryRepository).should().getCategory(1L);
        then(category).should().setName("new name");
        then(categoryRepository).should().saveCategory(category);
    }

    @Test
    void testDeleteCategory() {
        // given
        given(categoryRepository.deleteCategory(1L))
            .willReturn(1L);

        // when
        var id = categoryService.deleteCategory(1L);

        // then
        then(categoryRepository).should().deleteCategory(1L);
        assertThat(id).isEqualTo(1L);
    }
}
