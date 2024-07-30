package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.category.CategoryRepository;
import gift.category.CategoryService;
import gift.category.model.Category;
import gift.category.model.CategoryRequest;
import gift.product.ProductRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setup() {
        categoryService = new CategoryService(productRepository, categoryRepository);
    }

    @Test
    void getAllCategoriesTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "id"));
        given(categoryRepository.findAll(pageable)).willReturn(Page.empty());

        categoryService.getAllCategories(pageable);

        then(categoryRepository).should().findAll(pageable);
    }

    @Test
    void getCategoryTest() {
        given(categoryRepository.findById(any())).willReturn(
            Optional.of(new Category("test", "##test", "test.jpg", "test")));

        categoryService.getCategoryById(1L);

        then(categoryRepository).should().findById(any());
    }

    @Test
    void insertCategoryTest() {
        given(categoryRepository.save(any())).willReturn(
            new Category("test", "##test", "test.jpg", "test"));

        categoryService.insertCategory(
            new CategoryRequest("test", "##test", "test.jpg", "test"));

        then(categoryRepository).should().save(any());
    }

    @Test
    void updateCategoryTest() {
        Category category = new Category("test", "##test", "test.jpg", "test");
        given(categoryRepository.findById(any())).willReturn(Optional.of(category));

        categoryService.updateCategory(
            new CategoryRequest("test1", "##test1", "test1.jpg", "test1"), 1L);

        assertAll(
            () -> assertThat(category.getName()).isEqualTo("test1"),
            () -> assertThat(category.getColor()).isEqualTo("##test1"),
            () -> assertThat(category.getImageUrl()).isEqualTo("test1.jpg"),
            () -> assertThat(category.getDescription()).isEqualTo("test1")
        );
    }

    @Test
    void deleteCategoryTest() {
        categoryService.deleteCategory(1L);

        then(categoryRepository).should().deleteById(1L);
    }
}
