package gift.repository;

import gift.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class JpaCategoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 저장 테스트")
    void saveCategory() {
        Category category = new Category("name", "#aaaaaa", "https://asdf", "description");
        Category real = categoryRepository.save(category);

        assertAll(
                () -> assertThat(real.getId()).isNotNull(),
                () -> assertThat(real.getName()).isEqualTo(category.getName()),
                () -> assertThat(real.getColor()).isEqualTo(category.getColor()),
                () -> assertThat(real.getImageUrl()).isEqualTo(category.getImageUrl())
        );
    }
}
