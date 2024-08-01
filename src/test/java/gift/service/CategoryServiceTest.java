package gift.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import gift.dto.betweenClient.category.CategoryDTO;
import gift.entity.Category;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CategoryServiceTest {
    @Mock
    CategoryRepository categoryRepository;

    @Mock
    ProductRepository productRepository;

    CategoryService categoryService;

    List<Category> categoryList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryService(categoryRepository, productRepository);
        categoryList = new ArrayList<>();
        categoryList.add(new Category("테스트1", "#000001", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "테스트 1"));
        categoryList.add(new Category("테스트2", "#000002","https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "테스트2" ));
    }

    @Test
    void getCategories() {
        given(categoryRepository.findAll()).willReturn(categoryList);

        assertThatNoException().isThrownBy(() -> categoryService.getCategories());
    }

    @Test
    void addCategory() {
        given(categoryRepository.save(any())).willReturn(null);

        assertThatNoException().isThrownBy(() ->  categoryService.addCategory(CategoryDTO.convertToCategoryDTO(categoryList.getFirst())));
    }

    @Test
    void updateCategory() {
        given(categoryRepository.findById((Long) any())).willReturn(Optional.ofNullable(categoryList.getFirst()));
        given(categoryRepository.save(any())).willReturn(null);

        assertThatNoException()
                .isThrownBy(() ->
                        categoryService.updateCategory(1L, CategoryDTO.convertToCategoryDTO(categoryList.getLast())
                        ));
    }

    @Test
    void deleteCategory() {
        given(categoryRepository.save(any())).willReturn(null);
        doNothing().when(categoryRepository).deleteById(anyLong());

        assertThatNoException().isThrownBy(() ->  categoryService.addCategory(CategoryDTO.convertToCategoryDTO(categoryList.getFirst())));
        assertThatNoException().isThrownBy(() -> categoryService.deleteCategory(1L));
    }
}