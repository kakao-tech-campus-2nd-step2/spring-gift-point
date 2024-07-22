package gift.product.service;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

import gift.product.domain.Category;
import gift.product.exception.category.CategoryAlreadyExistException;
import gift.product.exception.category.CategoryNotFoundException;
import gift.product.persistence.CategoryRepository;
import gift.product.service.command.CategoryCommand;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Category 생성 테스트[성공]")
    void createCategoryTest() {
        // given
        CategoryCommand categoryCommand = new CategoryCommand("카테고리", "색상", "이미지 URL", "설명");
        Category savedCategory = new Category(1L, "카테고리", "색상", "이미지 URL", "설명");

        // when
        when(categoryRepository.save(any())).thenReturn(savedCategory);
        Long savedId = categoryService.createCategory(categoryCommand);

        // then
        assertThat(savedId).isEqualTo(1L);
        then(categoryRepository).should().save(any());
    }

    @Test
    @DisplayName("Category 생성 테스트[실패] 중복된 카테고리 이름")
    void createCategoryWithDuplicatedNameTest() {
        //given
        CategoryCommand categoryCommand = new CategoryCommand("카테고리", "색상", "이미지 URL", "설명");
        Category existParam = new Category(1L, "카테고리", "색상", "이미지 URL", "설명");

        //when
        when(categoryRepository.findByName(any())).thenReturn(of(existParam));

        //then
        assertThatThrownBy(() -> categoryService.createCategory(categoryCommand))
                .isInstanceOf(CategoryAlreadyExistException.class);
    }

    @Test
    @DisplayName("Category 수정 테스트[성공]")
    void modifyCategoryTest() {
        // given
        CategoryCommand categoryCommand = new CategoryCommand("새 카테고리", "새 색상", "새 이미지 URL", "새 설명");
        Category existCategory = new Category(1L, "카테고리", "색상", "이미지 URL", "설명");

        // when
        when(categoryRepository.findById(1L)).thenReturn(of(existCategory));
        when(categoryRepository.findByName(any())).thenReturn(Optional.empty());

        // then
        categoryService.modifyCategory(1L, categoryCommand);
        assertThat(existCategory).extracting("name", "color", "imgUrl", "description")
                .containsExactly("새 카테고리", "새 색상", "새 이미지 URL", "새 설명");
    }

    @Test
    @DisplayName("Category 수정 테스트[실패] 이미 존재하는 이름")
    void modifyCategoryWithDuplicatedNameTest() {
        // given
        CategoryCommand categoryCommand = new CategoryCommand("새 카테고리", "새 색상", "새 이미지 URL", "새 설명");
        Category existCategory = new Category(1L, "카테고리", "색상", "이미지 URL", "설명");
        Category existCategory2 = new Category(2L, "새 카테고리", "색상", "이미지 URL", "설명");

        // when
        when(categoryRepository.findByName(any())).thenReturn(of(existCategory2));

        // then
        assertThatThrownBy(() -> categoryService.modifyCategory(1L, categoryCommand))
                .isInstanceOf(CategoryAlreadyExistException.class);
    }

    @Test
    @DisplayName("Category 수정 테스트[실패] 존재하지 않는 카테고리의 Id")
    void modifyCategoryWithNotExistIdTest() {
        // given
        CategoryCommand categoryCommand = new CategoryCommand("새 카테고리", "새 색상", "새 이미지 URL", "새 설명");

        // when
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> categoryService.modifyCategory(1L, categoryCommand))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("Category 조회 테스트[성공]")
    void getCategoryInfoTest() {
        // given
        Category existCategory = new Category(1L, "카테고리", "색상", "이미지 URL", "설명");
        given(categoryRepository.findById(any())).willReturn(of(existCategory));

        // when
        var response = categoryService.getCategoryInfo(1L);

        //then
        assertThat(response).extracting(
                "id",
                "name",
                "color",
                "imgUrl",
                "description"
        ).containsExactly(
                existCategory.getId(),
                existCategory.getName(),
                existCategory.getColor(),
                existCategory.getImgUrl(),
                existCategory.getDescription()
        );
    }
}