package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import gift.domain.category.dto.CategoryRequest;
import gift.domain.category.dto.CategoryResponse;
import gift.domain.category.entity.Category;
import gift.domain.category.repository.CategoryRepository;
import gift.domain.category.service.CategoryService;
import gift.domain.option.repository.OptionRepository;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
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
    @Mock
    private ProductRepository productRepository;
    @Mock
    OptionRepository optionRepository;

    @Test
    @DisplayName("카테고리 전체 조회 테스트")
    void getAllCategoriesTest() {
        List<Category> categoryList = Arrays.asList(createCategory(), createCategory(2L));

        doReturn(categoryList).when(categoryRepository).findAll();

        List<CategoryResponse> expected = categoryList.stream().map(this::entityToDto).toList();

        // when
        List<CategoryResponse> actual = categoryService.getAllCategories();

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> IntStream.range(0, actual.size()).forEach(i -> {
                assertThat(actual.get(i).name())
                    .isEqualTo(expected.get(i).name());
                assertThat(actual.get(i).color())
                    .isEqualTo(expected.get(i).color());
                assertThat(actual.get(i).imageUrl())
                    .isEqualTo(expected.get(i).imageUrl());
                assertThat(actual.get(i).description())
                    .isEqualTo(expected.get(i).description());
            })
        );
    }

    @Test
    @DisplayName("카테고리 생성 테스트")
    void createCategoryTest() {
        // given
        CategoryRequest request = createCategoryRequest();
        Category newCategory = createCategory();

        doReturn(newCategory).when(categoryRepository).save(any());

        // when
        // then
        assertDoesNotThrow(
            () -> categoryService.createCategory(request)
        );
    }

    @Test
    @DisplayName("카테고리 수정 테스트")
    void updateCategoryTest() {
        // given
        Long id = 1L;
        CategoryRequest request = createCategoryRequest();
        Category updatedCategory = createCategory();
        Category spyUpdatedCategory = spy(updatedCategory);

        doReturn(Optional.of(spyUpdatedCategory)).when(categoryRepository).findById(any());
        doNothing().when(spyUpdatedCategory)
            .updateAll(request.getName(), request.getColor(), request.getImageUrl(),
                request.getDescription());

        // when
        // then
        assertDoesNotThrow(() -> categoryService.updateCategory(id, request));
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    void deleteCategoryTest() {
        Long id = 1L;
        Category savedCategory = createCategory();
        List<Product> productList = Arrays.asList(
            new Product(1L, "test", 100, "image", savedCategory),
            new Product(2L, "test", 100, "image", savedCategory));

        doReturn(Optional.of(savedCategory)).when(categoryRepository).findById(any(Long.class));
        doReturn(productList).when(productRepository).findAllByCategory(any(Category.class));
        doNothing().when(optionRepository).deleteByProduct(any(Product.class));
        doNothing().when(productRepository).deleteByCategory(any(Category.class));
        // when
        categoryService.deleteCategory(id);

        // then
        verify(optionRepository, times(2)).deleteByProduct(any(Product.class));
        verify(productRepository, times(1)).deleteByCategory(any(Category.class));
        verify(categoryRepository, times(1)).delete(any(Category.class));
    }

    private CategoryResponse entityToDto(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getColor(),
            category.getImageUrl(), category.getDescription());
    }

    private CategoryRequest createCategoryRequest() {
        return new CategoryRequest("name", "color", "imageUrl", "description");
    }

    private Category createCategory() {
        return createCategory(1L);
    }

    private Category createCategory(Long id) {
        return new Category(id, "name", "color", "imageUrl", "description");
    }
}