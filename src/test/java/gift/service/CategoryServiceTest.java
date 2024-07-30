package gift.service;

import gift.dto.request.AddCategoryRequest;
import gift.dto.request.UpdateCategoryRequest;
import gift.dto.response.CategoryIdResponse;
import gift.dto.response.CategoryResponse;
import gift.entity.Category;
import gift.exception.CategoryNameDuplicateException;
import gift.exception.CategoryNotFoundException;
import gift.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("카테고리 서비스 단위테스트")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName("모든 카테고리 조회")
    void getAllCategories() {
        //Given
        Category category1 = mock(Category.class);
        Category category2 = mock(Category.class);
        Category category3 = mock(Category.class);

        List<Category> categoryList = List.of(
                category1,
                category2,
                category3
        );

        when(category1.getName()).thenReturn("상품권");
        when(category2.getName()).thenReturn("교환권");
        when(category3.getName()).thenReturn("패션잡화");

        when(categoryRepository.findAll()).thenReturn(categoryList);

        //When
        List<CategoryResponse> categoryResponses = categoryService.getAllCategoryResponses();

        //Then
        assertThat(categoryResponses)
                .hasSize(3)
                .extracting("name")
                .containsExactly("상품권", "교환권", "패션잡화");
    }

    @Nested
    @DisplayName("카테고리 id로 조회")
    class Get {
        @Test
        @DisplayName("성공")
        void success() {
            //Given
            Category wantCategory = new Category("원하는 카테고리", "test", "test", "test");
            when(categoryRepository.findById(any())).thenReturn(Optional.of(wantCategory));

            //When
            Category result = categoryService.getCategory(any());

            //Then
            assertThat(result).isNotNull()
                    .extracting("name", "color")
                    .containsExactly("원하는 카테고리", "test");
        }

        @Test
        @DisplayName("실패 - 해당 카테고리 존재 안함")
        void fail() {
            //Given
            when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());

            //When Then
            assertThatThrownBy(() -> categoryService.getCategory(any(Long.class)))
                    .isInstanceOf(CategoryNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("카테고리 추가")
    class Add {
        @Test
        @DisplayName("성공")
        void success() {
            //Given
            when(categoryRepository.existsByName(any())).thenReturn(false);

            AddCategoryRequest request = new AddCategoryRequest("상품권", "색", "이미지주소", "설명");
            Category category = mock(Category.class);

            when(categoryRepository.save(any())).thenReturn(category);
            when(category.getId()).thenReturn(1L);

            //When
            CategoryIdResponse response = categoryService.addCategory(request);

            //Then
            assertThat(response.id()).isEqualTo(1L);
        }

        @Test
        @DisplayName("실패 - 카테고리 이름 중복")
        void fail() {
            //Given
            when(categoryRepository.existsByName(any())).thenReturn(true);
            AddCategoryRequest request = new AddCategoryRequest("상품권", "색", "이미지주소", "설명");

            //When Then
            assertThatThrownBy(() -> categoryService.addCategory(request))
                    .isInstanceOf(CategoryNameDuplicateException.class);
        }
    }

    @Nested
    @DisplayName("카테고리 수정")
    class Update {
        @Test
        @DisplayName("성공")
        void success() {
            //Given
            Category existingCategory = new Category("기존", "기존", "기존", "기존");

            UpdateCategoryRequest request = new UpdateCategoryRequest(1L, "새로움", "새로움", "뉴이미지", "설명");
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));

            //When
            categoryService.updateCategory(request);

            //Then
            assertThat(existingCategory.getName()).isEqualTo("새로움");
        }

        @Test
        @DisplayName("실패 - 해당 카테고리 존재 안함")
        void fail() {
            //Given
            when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

            UpdateCategoryRequest request = new UpdateCategoryRequest(1L, "새로움", "새로움", "뉴이미지", "설명");

            //When Then
            assertThatThrownBy(() -> categoryService.updateCategory(request))
                    .isInstanceOf(CategoryNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("카테고리 삭제")
    class Delete {
        @Test
        @DisplayName("성공")
        void success() {
            //Given
            Category deleteTargetCategory = new Category("타겟", "타겟", "타겟", "타겟");
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(deleteTargetCategory));

            //When
            categoryService.deleteCategory(1L);

            //Then
            verify(categoryRepository).delete(deleteTargetCategory);
        }

        @Test
        @DisplayName("실패 - 해당 카테고리 존재 안함")
        void fail() {
            //Given
            when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

            //When Then
            assertThatThrownBy(() -> categoryService.deleteCategory(1L))
                    .isInstanceOf(CategoryNotFoundException.class);
        }
    }
}
