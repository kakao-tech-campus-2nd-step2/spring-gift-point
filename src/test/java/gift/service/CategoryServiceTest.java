package gift.service;

import gift.domain.Category;
import gift.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CategoryServiceTest {

    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void getCategories() {
        // given
        Category category1 = new Category(1L, "category1");
        Category category2 = new Category(2L, "category2");
        given(categoryRepository.findAll()).willReturn(List.of(category1, category2));

        // when
        List<Category> categories = categoryService.getCategories();

        // then
        Assertions.assertThat(categories.size()).isEqualTo(2);
    }
}