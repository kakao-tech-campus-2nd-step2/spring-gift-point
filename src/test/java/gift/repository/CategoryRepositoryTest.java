package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.domain.Category;
import gift.repository.fixture.CategoryFixture;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private EntityManager em;

    @Test
    void 카테고리_저장() {
        // given
        Category expected = CategoryFixture.createCategory("name", "color", "description",
            "imageUrl");
        // when
        Category actual = categoryRepository.save(expected);
        em.flush();
        em.clear();
        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull()
            );
    }

    @Test
    void 모든_카테고리_검색() {
        // given
        categoryRepository.save(
            CategoryFixture.createCategory("test1", "color", "description", "imageUrl"));
        categoryRepository.save(
            CategoryFixture.createCategory("test2", "color", "description", "imageUrl"));
        em.flush();
        em.clear();
        // when
        List<Category> actuals = categoryRepository.findAll();
        // then
        assertAll(
            () -> assertThat(actuals.size()).isEqualTo(2)
        );
    }

    @Test
    void 특정_카테고리_검색() {
        // given
        Category expected = CategoryFixture.createCategory("test1", "color", "description",
            "imageUrl");
        categoryRepository.save(expected);
        em.flush();
        em.clear();
        // when
        Category actual = categoryRepository.save(expected);
        em.flush();
        em.clear();
        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getColor()).isEqualTo(expected.getColor()),
            () -> assertThat(actual.getDescription()).isEqualTo(expected.getDescription()),
            () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl())
        );
    }
}
