package gift.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    /*
    - [] 카테고리 추가하기
    - [] 업데이트 할 카테고리가 없는 경우
    - [] 모든 카테고리 확인
    - [] 카테고리 삭제
     */

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void createCategory() {
        //given
        CategoryRequest expected = categoryRequest();
        doReturn(expected.toEntity()).when(categoryRepository).save(any(Category.class));

        //when
        Category actual = categoryService.createCategory(expected);

        //then
        assertAll(
            () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getColor()).isEqualTo(expected.getColor()),
            () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl()),
            () -> assertThat(actual.getDescription()).isEqualTo(expected.getDescription())
        );

    }

    @DisplayName("업데이트 할 카테고리가 없는 경우")
    @Test
    void updateCategory() {
        //given
        CategoryRequest request = categoryRequest();

        //when , then
        assertThrows(NoSuchElementException.class, () -> categoryService.updateCategory(request));
    }

    @Test
    void getAllCategories() {
        //when
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            categories.add(new Category("name" + i, "color" + i, "image" + i, ""));
        }
        doReturn(categories).when(categoryRepository).findAll();

        //given
        final List<Category> result = categoryService.getAllCategories();

        //then
        assertThat(result.size()).isEqualTo(5);

    }

    @Test
    void deleteCategory() {
        //given
        Long id = 1L;

        //when
        categoryService.deleteCategory(id);

        //then
        verify(categoryRepository, times(1)).deleteById(any(Long.class));
    }

    private CategoryRequest categoryRequest() {
        return new CategoryRequest("category", "color", "image", "");
    }

}