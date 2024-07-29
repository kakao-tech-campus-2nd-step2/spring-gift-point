package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Category;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        Category expected = new Category("음식", "testColor", "testImage.jpg", "TestDescription");
        Category actual = categoryRepository.save(expected);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    @Test
    @DisplayName("DB에 저장된 ID를 기반으로 저장된 카테고리를 불러오는지 테스트")
    void findByIdTest() {
        Long First_Category_ID = 1L;
        Optional<Category> actual = categoryRepository.findById(First_Category_ID);

        assertThat(actual).isPresent().hasValueSatisfying(
            w -> assertThat(w.getDescription()).isEqualTo("교환권 카테고리입니다."));
    }

    @Test
    @DisplayName("페이지네이션 테스트")
    void PaginationTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> page = categoryRepository.findAll(pageable);

        assertThat(page.getTotalElements()).isEqualTo(13);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(10);

        assertThat(page.getContent().get(0).getName()).isEqualTo("교환권");
    }
}
