package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.entity.Category;
import gift.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 추가 테스트")
    public void addCategoryTest() {
        Category category = new Category("교환권", "색상", "이미지URL", "설명");
        categoryRepository.save(category);

        Optional<Category> categoryOp = categoryRepository.findById(category.getId());
        assertThat(categoryOp).isPresent();
        assertThat(categoryOp.get().getName()).isEqualTo(category.getName());
        assertThat(categoryOp.get().getColor()).isEqualTo(category.getColor());
        assertThat(categoryOp.get().getImageUrl()).isEqualTo(category.getImageUrl());
        assertThat(categoryOp.get().getDescription()).isEqualTo(category.getDescription());
    }

    @Test
    @DisplayName("카테고리 조회 테스트")
    void findAll() {
        for (int i = 0; i < 5; i++) {
            categoryRepository.save(new Category("카테고리" + i, "색상" + i, "이미지URL" + i, "설명" + i));
        }
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(5);
    }

    @Test
    void save() {
        Category expected = new Category("교환권", "색상", "이미지URL", "설명");
        Category actual = categoryRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getColor()).isEqualTo(expected.getColor()),
            () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl()),
            () -> assertThat(actual.getDescription()).isEqualTo(expected.getDescription())
        );
    }

    @Test
    public void findByName() {
        String expected = "교환권";
        categoryRepository.save(new Category(expected, "색상", "이미지URL", "설명"));
        boolean exist = categoryRepository.existsByName(expected);
        assertThat(exist).isTrue();
    }


}
