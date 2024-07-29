package gift.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import gift.model.product.Category;
import gift.repository.product.CategoryRepository;
import gift.application.product.service.CategoryService;
import gift.application.product.dto.CategoryCommand;
import gift.application.product.dto.CategoryModel;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceMockTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 생성 테스트")
    void 카테고리_생성() {
        // given
        CategoryCommand.Register command = new CategoryCommand.Register("카테고리", "red", "설명", "url");
        given(categoryRepository.save(any(Category.class))).willReturn(
            new Category("카테고리", "red", "설명", "url"));

        // when
        final CategoryModel.Info result = categoryService.createCategory(command);
        // then
        assertThat(result.name()).isEqualTo("카테고리");
    }


    @Test
    @DisplayName("카테고리 생성 - 중복된 이름으로 인한 실패 테스트")
    void 카테고리_생성_중복_실패() {
        // given
        CategoryCommand.Register command = new CategoryCommand.Register("카테고리", "red", "설명", "url");
        given(categoryRepository.findByName(command.name())).willReturn(
            Optional.of(new Category("카테고리", "red", "설명", "url")));

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.createCategory(command);
        });
    }


}
