package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

        int pageNo = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Category> categoryPage = new PageImpl<>(categoryList, pageable, categoryList.size());

        doReturn(categoryPage).when(categoryRepository).findAll(pageable);

        Page<CategoryResponse> expected = categoryPage.map(this::entityToDto);

        // when
        Page<CategoryResponse> actual = categoryService.getAllCategories(pageNo, pageSize);

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> IntStream.range(0, actual.getContent().size()).forEach(i -> {
                assertThat(actual.getContent().get(i).name())
                    .isEqualTo(expected.getContent().get(i).name());
                assertThat(actual.getContent().get(i).color())
                    .isEqualTo(expected.getContent().get(i).color());
                assertThat(actual.getContent().get(i).imageUrl())
                    .isEqualTo(expected.getContent().get(i).imageUrl());
                assertThat(actual.getContent().get(i).description())
                    .isEqualTo(expected.getContent().get(i).description());
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

        CategoryResponse expected = entityToDto(newCategory);

        // when
        CategoryResponse actual = categoryService.createCategory(request);

        // then
        assertAll(
            () -> assertThat(actual.id()).isEqualTo(expected.id()),
            () -> assertThat(actual.name()).isEqualTo(expected.name()),
            () -> assertThat(actual.color()).isEqualTo(expected.color()),
            () -> assertThat(actual.imageUrl()).isEqualTo(expected.imageUrl()),
            () -> assertThat(actual.description()).isEqualTo(expected.description())
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

        CategoryResponse expected = entityToDto(updatedCategory);

        // when
        CategoryResponse actual = categoryService.updateCategory(id, request);

        // then
        assertAll(
            () -> assertThat(actual.id()).isEqualTo(expected.id()),
            () -> assertThat(actual.name()).isEqualTo(expected.name()),
            () -> assertThat(actual.color()).isEqualTo(expected.color()),
            () -> assertThat(actual.imageUrl()).isEqualTo(expected.imageUrl()),
            () -> assertThat(actual.description()).isEqualTo(expected.description())
        );
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