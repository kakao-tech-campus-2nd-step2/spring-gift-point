package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import gift.converter.StringToUrlConverter;
import gift.domain.Category;
import gift.domain.Category.Builder;
import gift.domain.vo.Color;
import gift.repository.CategoryRepository;
import gift.web.dto.request.category.CreateCategoryRequest;
import gift.web.dto.request.category.UpdateCategoryRequest;
import gift.web.dto.response.category.CreateCategoryResponse;
import gift.web.dto.response.category.ReadAllCategoriesResponse;
import gift.web.dto.response.category.ReadCategoryResponse;
import gift.web.dto.response.category.UpdateCategoryResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName("생성 요청이 정상적일 때, 카테고리를 성공적으로 생성합니다.")
    void createCategory() {
        //given
        CreateCategoryRequest request = new CreateCategoryRequest("카테고리01", "카테고리01 설명", "https://www.google.com", "#FFFFFF");
        Category category = request.toEntity();
        given(categoryRepository.save(any(Category.class))).willReturn(category);

        //when
        CreateCategoryResponse response = categoryService.createCategory(request);

        //then
        assertAll(
            () -> assertThat(response.getName()).isEqualTo(category.getName()),
            () -> assertThat(response.getDescription()).isEqualTo(category.getDescription()),
            () -> assertThat(response.getImageUrl()).isEqualTo(category.getImageUrl().toString()),
            () -> assertThat(response.getColor()).isEqualTo(category.getColor().toString())
        );
    }

    @Test
    @DisplayName("모든 카테고리 조회 요청이 정상적일 때, 모든 카테고리를 성공적으로 조회합니다.")
    void readAllCategories() {
        //given
        List<Category> categories = List.of(
            new Category.Builder().name("카테고리01").description("카테고리01 설명").imageUrl(StringToUrlConverter.convert("https://www.google.com")).color(Color.from("#FFFFFF")).build(),
            new Category.Builder().name("카테고리02").description("카테고리02 설명").imageUrl(StringToUrlConverter.convert("https://www.google.com")).color(Color.from("#FFFFFF")).build(),
            new Category.Builder().name("카테고리03").description("카테고리03 설명").imageUrl(StringToUrlConverter.convert("https://www.google.com")).color(Color.from("#FFFFFF")).build()
        );
        Page<Category> page = new PageImpl<>(categories);
        given(categoryRepository.findAll(any(PageRequest.class))).willReturn(page);

        //when
        ReadAllCategoriesResponse response = categoryService.readAllCategories(PageRequest.of(0, 10));

        //then
        List<ReadCategoryResponse> actual = response.getCategories();
        Assertions.assertAll(
            () -> assertThat(actual).hasSize(3),
            () -> assertThat(actual.get(0).getName()).isEqualTo(categories.get(0).getName()),
            () -> assertThat(actual.get(0).getDescription()).isEqualTo(categories.get(0).getDescription()),
            () -> assertThat(actual.get(0).getImageUrl()).isEqualTo(categories.get(0).getImageUrl().toString()),
            () -> assertThat(actual.get(0).getColor()).isEqualTo(categories.get(0).getColor().toString()),

            () -> assertThat(actual.get(1).getName()).isEqualTo(categories.get(1).getName()),
            () -> assertThat(actual.get(1).getDescription()).isEqualTo(categories.get(1).getDescription()),
            () -> assertThat(actual.get(1).getImageUrl()).isEqualTo(categories.get(1).getImageUrl().toString()),
            () -> assertThat(actual.get(1).getColor()).isEqualTo(categories.get(1).getColor().toString()),

            () -> assertThat(actual.get(2).getName()).isEqualTo(categories.get(2).getName()),
            () -> assertThat(actual.get(2).getDescription()).isEqualTo(categories.get(2).getDescription()),
            () -> assertThat(actual.get(2).getImageUrl()).isEqualTo(categories.get(2).getImageUrl().toString()),
            () -> assertThat(actual.get(2).getColor()).isEqualTo(categories.get(2).getColor().toString())
        );
    }

    @Test
    @DisplayName("단일 카테고리 조회 요청이 정상적일 때, 카테고리를 성공적으로 조회합니다.")
    void readCategory() {
        //given
        Category category = new Builder()
            .id(1L)
            .name("카테고리01")
            .description("카테고리01 설명")
            .imageUrl(StringToUrlConverter.convert("https://www.google.com"))
            .color(Color.from("#FFFFFF"))
            .build();
        given(categoryRepository.findById(any(Long.class))).willReturn(Optional.of(category));

        //when
        ReadCategoryResponse response = categoryService.readCategory(1L);

        //then
        assertAll(
            () -> assertThat(response.getId()).isEqualTo(category.getId()),
            () -> assertThat(response.getName()).isEqualTo(category.getName()),
            () -> assertThat(response.getDescription()).isEqualTo(category.getDescription()),
            () -> assertThat(response.getImageUrl()).isEqualTo(category.getImageUrl().toString()),
            () -> assertThat(response.getColor()).isEqualTo(category.getColor().toString())
        );
    }

    @Test
    @DisplayName("카테고리 수정 요청이 정상적일 때, 카테고리를 성공적으로 수정합니다.")
    void updateCategory() {
        //given
        UpdateCategoryRequest request = new UpdateCategoryRequest("카테고리01",
            "카테고리01 설명", "https://www.google.com", "#FFFFFF");
        given(categoryRepository.findById(any(Long.class))).willReturn(Optional.of(new Category.Builder().build()));

        //when
        UpdateCategoryResponse response = categoryService.updateCategory(1L, request);

        //then
        assertAll(
            () -> assertThat(response.getName()).isEqualTo(request.getName()),
            () -> assertThat(response.getDescription()).isEqualTo(request.getDescription()),
            () -> assertThat(response.getImageUrl()).isEqualTo(request.getImageUrl()),
            () -> assertThat(response.getColor()).isEqualTo(request.getColor())
        );
    }

    @Test
    @DisplayName("카테고리 삭제 요청이 정상적일 때, 카테고리를 성공적으로 삭제합니다.")
    void deleteCategory() {
        //given
        Long categoryId = 1L;

        //when
        categoryService.deleteCategory(categoryId);

        //then
        Assertions.assertDoesNotThrow(() -> categoryRepository.findById(1L));
    }
}