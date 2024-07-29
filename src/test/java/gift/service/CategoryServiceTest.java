package gift.service;

import gift.domain.Category;
import gift.dto.CategoryDto;
import gift.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @DisplayName("카테고리를 전체 조회한다.")
    @Test
    void getCategories() throws Exception {
        //given
        given(categoryRepository.findAll()).willReturn(List.of());

        //when
        categoryService.getCategories();

        //then
        then(categoryRepository).should().findAll();
    }

    @DisplayName("카테고리 하나를 추가한다.")
    @Test
    void addCategory() throws Exception {
        //given
        CategoryDto dto = new CategoryDto("교환권");

        given(categoryRepository.save(any(Category.class))).willReturn(new Category());
        given(categoryRepository.existsByName(anyString())).willReturn(false);

        //when
        categoryService.addCategory(dto);

        //then
        then(categoryRepository).should().save(any(Category.class));
        then(categoryRepository).should().existsByName(anyString());
    }

    @DisplayName("카테고리 ID를 받아, 해당하는 카테고리 이름을 수정한다.")
    @Test
    void editCategory() throws Exception {
        //given
        Long categoryId = 1L;
        CategoryDto dto = new CategoryDto("교환권");

        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(new Category("상품권")));
        given(categoryRepository.existsByName(anyString())).willReturn(false);

        //when
        categoryService.editCategory(categoryId, dto);

        //then
        then(categoryRepository).should().findById(categoryId);
        then(categoryRepository).should().existsByName(anyString());
    }

    @DisplayName("카테고리 ID를 받아, 해당하는 카테고리를 삭제한다.")
    @Test
    void removeCategory() throws Exception {
        //given
        Long categoryId = 1L;

        willDoNothing().given(categoryRepository).deleteById(categoryId);

        //when
        categoryService.removeCategory(categoryId);

        //then
        then(categoryRepository).should().deleteById(categoryId);
    }

}
