package gift.repository;

import gift.config.JpaConfig;
import gift.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 존재 확인 테스트[성공]")
    void existsByName() {
        // given
        String name = "카테고리";
        String color = "#123456";
        String imageUrl = "이미지url";
        String description = "설명";
        Category category = new Category(name, color, imageUrl, description);
        categoryRepository.save(category);

        // when
        boolean exists = categoryRepository.existsByName(name);

        // then
        assertThat(exists).isTrue();
    }
}