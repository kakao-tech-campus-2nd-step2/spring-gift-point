package gift.service;

import gift.DTO.Category.CategoryRequest;
import gift.DTO.Category.CategoryResponse;
import gift.TestUtil;
import gift.domain.Category;
import gift.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName("saveTest")
    void test1(){
        // given
        ArgumentCaptor<Category> captor_c = ArgumentCaptor.forClass(Category.class);

        CategoryRequest categoryRequest = new CategoryRequest("신규");
        Category category = new Category(categoryRequest.getName());
        given(categoryRepository.save(captor_c.capture())).willAnswer(invocation -> captor_c.getValue());
        // when
        CategoryResponse savedCategory = categoryService.save(categoryRequest);
        // then
        Assertions.assertThat(savedCategory.getName()).isEqualTo("신규");
    }

    @Test
    @DisplayName("findAllTest")
    void test2(){
        // given
        List<Category> categories = new ArrayList<>();
        for(int i = 1; i <= 5; i++){
            Category category = new Category("신규" + i);
            categories.add(category);
        }

        given(categoryRepository.findAll()).willAnswer(invocation -> categories);
        // when
        List<CategoryResponse> savedCategories = categoryService.findAll();
        // then
        Assertions.assertThat(savedCategories).isNotNull();
        Assertions.assertThat((long) savedCategories.size()).isEqualTo(5);
        for(int i = 1; i <= 5; i++){
            String name = savedCategories.get(i-1).getName();
            Assertions.assertThat(name).isEqualTo("신규"+i);
        }
    }

    @Test
    @DisplayName("updateTest")
    void test4(){
        // given
        CategoryRequest categoryRequest = new CategoryRequest("신규 카테고리");
        Category category = new Category("신규");
        TestUtil.setId(category, 1L);
        given(categoryRepository.findById(1L)).willAnswer(invocation -> Optional.of(category));
        // when
        categoryService.update(1L, categoryRequest);
        // then
        Assertions.assertThat(category.getName()).isEqualTo("신규 카테고리");
    }

    @Test
    @DisplayName("deleteTest")
    void test5(){
        // when
        categoryService.delete(1L);
        // then
        then(categoryRepository).should(times(1)).deleteById(1L);
    }
}
