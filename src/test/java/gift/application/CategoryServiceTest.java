package gift.application;

import gift.product.application.CategoryService;
import gift.product.dao.CategoryRepository;
import gift.product.dto.CategoryRequest;
import gift.product.dto.CategoryResponse;
import gift.product.entity.Category;
import gift.product.util.CategoryMapper;
import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testFixtures.CategoryFixture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 리스트 조회 서비스 테스트")
    void getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        Category category1 = CategoryFixture.createCategory("상품권");
        Category category2 = CategoryFixture.createCategory("교환권");
        categoryList.add(category1);
        categoryList.add(category2);
        given(categoryRepository.findAll())
                .willReturn(categoryList);

        List<CategoryResponse> responses = categoryService.getAllCategories();

        assertThat(responses.size()).isEqualTo(categoryList.size());
    }

    @Test
    @DisplayName("카테고리 ID 조회 서비스 테스트")
    void getCategoryByIdOrThrow() {
        Category category = CategoryFixture.createCategory("상품권");
        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(category));

        CategoryResponse response = categoryService.getCategoryByIdOrThrow(1L);

        assertThat(response.name()).isEqualTo(category.getName());
    }

    @Test
    @DisplayName("카테고리 ID 조회 실패 테스트")
    void getCategoryByIdOrThrowFailed() {
        Long categoryId = 1L;
        given(categoryRepository.findById(any()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryByIdOrThrow(categoryId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.CATEGORY_NOT_FOUND
                                     .getMessage());
    }

    @Test
    @DisplayName("카테고리 추가 서비스 테스트")
    void createCategory() {
        CategoryRequest request = new CategoryRequest(
                "상품권",
                "#ffffff",
                "https://product-shop.com",
                ""
        );
        Category category = CategoryMapper.toEntity(request);
        given(categoryRepository.save(any()))
                .willReturn(category);

        CategoryResponse response = categoryService.createCategory(request);

        assertThat(response.name()).isEqualTo(category.getName());
    }

    @Test
    @DisplayName("카테고리 삭제 서비스 테스트")
    void deleteCategoryById() {
        Long categoryId = 1L;

        categoryService.deleteCategoryById(categoryId);

        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    @DisplayName("카테고리 수정 서비스 테스트")
    void updateCategory() {
        CategoryRequest request = new CategoryRequest(
                "상품권",
                "#ffffff",
                "https://product-shop.com",
                ""
        );
        Category category = CategoryFixture.createCategory("교환권");
        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(category));

        categoryService.updateCategory(category.getId(), request);

        assertThat(category.getName()).isEqualTo(request.name());
    }

    @Test
    @DisplayName("카테고리 수정 실패 테스트")
    void updateCategoryFailed() {
        Long categoryId = 1L;
        CategoryRequest request = new CategoryRequest(
                "상품권",
                "#ffffff",
                "https://product-shop.com",
                ""
        );
        given(categoryRepository.findById(any()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.updateCategory(categoryId, request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.CATEGORY_NOT_FOUND
                                     .getMessage());
    }

}