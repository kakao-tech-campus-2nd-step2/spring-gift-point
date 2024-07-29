package gift.service;

import gift.dto.category.CategoryRequest;
import gift.exception.DuplicatedNameException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    private final Pageable pageable = PageRequest.of(0, 10);
    @Autowired
    private CategoryService categoryService;

    @Test
    @DisplayName("정상 카테고리 추가하기")
    void successAddCategory() {
        //given
        var categoryRequest = new CategoryRequest("상품카테고리", "상품설명", "#111111", "이미지");
        //when
        var savedCategory = categoryService.addCategory(categoryRequest);
        //then
        Assertions.assertThat(savedCategory.name()).isEqualTo("상품카테고리");

        categoryService.deleteCategory(savedCategory.id());
    }

    @Test
    @DisplayName("중복된 이름의 카테고리 추가하기")
    void failAddCategoryWithExistsName() {
        //given
        var categoryRequest = new CategoryRequest("상품카테고리", "상품설명", "#111111", "이미지");
        var category = categoryService.addCategory(categoryRequest);
        //when, then
        Assertions.assertThatThrownBy(() -> categoryService.addCategory(categoryRequest)).isInstanceOf(DuplicatedNameException.class);

        categoryService.deleteCategory(category.id());
    }

    @Test
    @DisplayName("단일 카테고리 조회하기")
    void successGetCategory() {
        //given
        var CategoryId = 1L;
        //when
        var category = categoryService.getCategory(CategoryId);
        //then
        Assertions.assertThat(category.name()).isEqualTo("디지털/가전");
    }

    @Test
    @DisplayName("전체 카테고리 조회하기")
    void successGatCategories() {
        //given, when
        var categories = categoryService.getCategories(pageable);
        //then
        Assertions.assertThat(categories.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("카테고리 정보 변경하기")
    void successUpdateCategory() {
        //given
        var categoryRequest = new CategoryRequest("상품카테고리", "상품설명", "#111111", "이미지");
        var savedCategory = categoryService.addCategory(categoryRequest);
        var updateRequest = new CategoryRequest("상품카테고리-수정", "상품설명-수정", "#111111", "이미지-수정");
        //when
        categoryService.updateCategory(savedCategory.id(), updateRequest);
        //then
        var updatedCategory = categoryService.getCategory(savedCategory.id());
        Assertions.assertThat(updatedCategory.name()).isEqualTo("상품카테고리-수정");

        categoryService.deleteCategory(savedCategory.id());
    }
}
