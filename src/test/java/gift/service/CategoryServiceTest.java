package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import gift.administrator.category.Category;
import gift.administrator.category.CategoryDTO;
import gift.administrator.category.CategoryRepository;
import gift.administrator.category.CategoryService;
import gift.error.NotFoundIdException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CategoryServiceTest {

    private CategoryService categoryService;
    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void beforeEach() {
        categoryService = new CategoryService(categoryRepository);
        category = new Category("상품권", "#ff11ff", "image.jpg", "");
        categoryDTO = new CategoryDTO("상품권", "#ff11ff", "image.jpg", "");
    }

    @Test
    @DisplayName("카테고리 전체 조회 테스트")
    void getAllCategories() {
        //given
        Category category1 = new Category("인형", "#dd11ff", "image.jpg", "none");
        given(categoryRepository.findAll()).willReturn(
            Arrays.asList(category, category1));
        CategoryDTO expected = new CategoryDTO("상품권", "#ff11ff", "image.jpg", "");
        CategoryDTO expected1 = new CategoryDTO("인형", "#dd11ff", "image.jpg", "none");

        //when
        List<CategoryDTO> actual = categoryService.getAllCategories();

        //then
        assertThat(actual).hasSize(2);
        assertThat(actual)
            .extracting(CategoryDTO::getName, CategoryDTO::getColor, CategoryDTO::getImageUrl,
                CategoryDTO::getDescription)
            .containsExactly(
                tuple(expected.getName(), expected.getColor(), expected.getImageUrl(),
                    expected.getDescription()),
                tuple(expected1.getName(), expected1.getColor(), expected1.getImageUrl(),
                    expected1.getDescription())
            );
    }

    @Test
    @DisplayName("아이디로 카테고리 검색")
    void getCategoryById() {
        //given
        given(categoryRepository.findById(any())).willReturn(
            Optional.of(category));
        CategoryDTO expected = new CategoryDTO("상품권", "#ff11ff", "image.jpg", "");

        //when
        CategoryDTO actual = categoryService.getCategoryById(1L);

        //then
        assertThat(actual)
            .extracting(CategoryDTO::getName, CategoryDTO::getColor, CategoryDTO::getImageUrl,
                CategoryDTO::getDescription)
            .containsExactly(expected.getName(), expected.getColor(), expected.getImageUrl(),
                expected.getDescription());
    }

    @Test
    @DisplayName("아이디로 카테고리 검색시 없는 아이디일 때 오류")
    void getCategoryByIdNotFoundException() {
        //given
        given(categoryRepository.findById(any())).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> categoryService.getCategoryById(1L)).isInstanceOf(
            NotFoundIdException.class).hasMessageContaining("카테고리 아이디를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("카테고리 추가 시 이미 존재하는 이름이면 오류")
    void addCategorySameNameError() {
        //given
        given(categoryRepository.existsByName(any())).willReturn(true);

        //when

        //then
        assertThatIllegalArgumentException().isThrownBy(
            () -> categoryService.addCategory(categoryDTO)).withMessage("존재하는 이름입니다.");
    }

    @Test
    @DisplayName("카테고리 추가 성공")
    void addCategory() {
        //given
        given(categoryRepository.existsByName(any())).willReturn(false);
        given(categoryRepository.save(any())).willReturn(category);

        //when
        categoryService.addCategory(categoryDTO);

        //then
        then(categoryRepository).should().save(any());
    }

    @Test
    @DisplayName("카테고리 업데이트시 없는 아이디일 때 오류")
    void updateCategoryNotFoundException() {
        //given
        given(categoryRepository.findById(1L)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> categoryService.updateCategory(categoryDTO, 1L)).isInstanceOf(
            NotFoundIdException.class).hasMessageContaining("카테고리 아이디를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("카테고리 업데이트 존재하는 이름일 때 오류")
    void updateCategoryExistingName() {
        //given
        given(categoryRepository.findById(any())).willReturn(Optional.of(category));
        given(categoryRepository.existsByNameAndIdNot(anyString(), anyLong())).willReturn(true);

        //when

        //then
        assertThatIllegalArgumentException().isThrownBy(
            () -> categoryService.updateCategory(categoryDTO, 1L)).withMessage("존재하는 이름입니다.");
    }

    @Test
    @DisplayName("카테고리 업데이트 성공")
    void updateCategory() {
        //given
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(categoryRepository.existsByName(any())).willReturn(false);
        given(categoryRepository.save(any())).willReturn(category);

        //when
        categoryService.updateCategory(categoryDTO, 1L);

        //then
        then(categoryRepository).should().save(any());
    }

    @Test
    @DisplayName("카테고리 이름 존재함 Exception 던짐")
    void existsByNameThrowException() {
        //given
        given(categoryRepository.existsByName(anyString())).willReturn(true);

        //when

        //then
        assertThatIllegalArgumentException().isThrownBy(
                () -> categoryService.existsByNameThrowException("도서"))
            .withMessage("존재하는 이름입니다.");
    }

    @Test
    @DisplayName("카테고리 이름 존재하지 않음 Exception 던지지 않음")
    void notExistingByNameNotThrowException() {
        //given
        given(categoryRepository.existsByName(anyString())).willReturn(false);

        //when

        //then
        assertDoesNotThrow(() -> categoryService.existsByNameThrowException("라면"));
    }

    @Test
    @DisplayName("카테고리 이름 존재하고 아이디는 다름 Exception 던짐")
    void existsByNameAndIdThrowException() {
        //given
        given(categoryRepository.existsByName(anyString())).willReturn(true);
        given(categoryRepository.findById(any())).willReturn(Optional.ofNullable(category));

        //when

        //then
        assertThatIllegalArgumentException().isThrownBy(
                () -> categoryService.existsByNameThrowException("상품권"))
            .withMessage("존재하는 이름입니다.");
    }

    @Test
    @DisplayName("카테고리 이름 존재하지 않거나 존재하지만 아이디가 같음 Exception 던지지 않음")
    void existsByNameAndIdNotThrowException() {
        //given
        given(categoryRepository.existsByName(anyString())).willReturn(false);
        given(categoryRepository.findById(any())).willReturn(Optional.ofNullable(category));

        //when

        //then
        assertDoesNotThrow(() -> categoryService.existsByNameAndId("라면", 1L));
    }

    @Test
    @DisplayName("카테고리 삭제")
    void deleteCategory() {
        //given
        given(categoryRepository.existsById(anyLong())).willReturn(true);

        //when
        categoryService.deleteCategory(1L);

        //then
        then(categoryRepository).should().deleteById(any());
    }

    @Test
    @DisplayName("카테고리 삭제시 아이디가 없는 오류")
    void deleteCategoryNotFoundIdException() {
        //given
        given(categoryRepository.findById(any())).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> categoryService.deleteCategory(1L)).isInstanceOf(
            NotFoundIdException.class).hasMessageContaining("삭제하려는 카테고리가 존재하지 않습니다.");
    }
}
