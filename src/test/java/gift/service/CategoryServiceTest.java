package gift.service;

import static gift.util.CategoryFixture.createCategory;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.domain.Category;
import gift.dto.category.CategoryDto;
import gift.repository.CategoryRepository;
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

    private Long id;
    private Category category;

    @BeforeEach
    void setup() {
        id = 1L;
        category = createCategory(id, "test");
    }

    @DisplayName("카테고리 추가")
    @Test
    void addCategory() {
        // given
        given(categoryRepository.save(any(Category.class))).willReturn(category);

        // when
        CategoryDto actual = categoryService.addCategory(category.toDto());

        // then
        assertThat(actual).isEqualTo(category.toDto());
    }

    @DisplayName("id로 카테고리 찾기")
    @Test
    void getProduct() {
        // given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        CategoryDto actual = categoryService.getCategory(id);

        // then
        assertThat(actual).isEqualTo(category.toDto());
    }

    @DisplayName("카테고리 수정")
    @Test
    void updateProduct() {
        // given
        Category updatedCategory = createCategory(id, "updatedTest");
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));
        given(categoryRepository.save(any(Category.class))).willReturn(updatedCategory);

        // when
        CategoryDto actual = categoryService.updateCategory(id, updatedCategory.toDto());

        // then
        assertThat(actual).isEqualTo(updatedCategory.toDto());
    }

    @DisplayName("카테고리 삭제")
    @Test
    void deleteProduct() {
        // given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        categoryService.deleteCategory(id);

        // then
        then(categoryRepository).should().delete(any(Category.class));
    }
}
