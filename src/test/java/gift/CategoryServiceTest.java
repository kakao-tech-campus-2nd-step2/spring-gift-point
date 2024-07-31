package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gift.dto.CategoryRequestDto;
import gift.entity.Category;
import gift.repository.CategoryRepository;
import gift.service.CategoryService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName("모든 카테고리 조회 테스트")
    public void findAllTest() {

        Category category1 = new Category("교환권", "컬러1", "이미지1", "설명1");
        Category category2 = new Category("기프티콘", "컬러2", "이미지2", "설명2");
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<Category> categories = categoryService.findAll();

        assertThat(categories).hasSize(2);
        assertThat(categories.get(0).getName()).isEqualTo("교환권");
        assertThat(categories.get(1).getName()).isEqualTo("기프티콘");
    }

    @Test
    @DisplayName("카테고리 ID로 조회 테스트")
    public void findByIdTest() {
        Long id = 1L;
        Category category = new Category("교환권", "컬러", "이미지", "설명");
        category.setId(id);
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        Optional<Category> categoryOp = categoryService.findById(id);

        assertThat(categoryOp).isPresent();
        assertThat(categoryOp.get().getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("카테고리 저장 테스트")
    public void saveTest() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("교환권", "컬러", "이미지", "설명");
        Category category = categoryRequestDto.toEntity();
        category.setId(1L);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category savedCategory = categoryService.save(categoryRequestDto);

        assertThat(savedCategory.getId()).isEqualTo(1L);
        assertThat(savedCategory.getName()).isEqualTo("교환권");
    }

    @Test
    @DisplayName("카테고리 이름 존재 여부 테스트")
    public void existsByNameTest() {
        String name = "교환권";
        when(categoryRepository.existsByName(name)).thenReturn(true);

        boolean exists = categoryService.existsByName(name);

        assertThat(exists).isTrue();
    }
}
