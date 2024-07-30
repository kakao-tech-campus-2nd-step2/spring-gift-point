package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.exception.ErrorCode;
import gift.exception.RepositoryException;
import gift.model.Category;
import gift.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categories;
    private final Pageable pageable = PageRequest.of(0, 10);

    @Test
    void save() {
        var expected = new Category(1L, "test-category", "000000", "test-image.url", "");
        var actual = categories.save(expected);
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    @Test
    void find() {
        var category1 = new Category(1L, "food", "000000", "test-image.url", "");
        var category2 = new Category(2L, "drink", "000000", "test-image.url", "");
        categories.save(category1);
        categories.save(category2);

        assertAll(
            () -> assertThat(categories.findById(1L, pageable)
                .stream()
                .toList()
                .size())
                .isEqualTo(1),
            () -> assertThat(categories.findAll(pageable)
                .stream()
                .toList()
                .size())
                .isEqualTo(2)
        );
    }

    @Test
    void update() {
        var current = new Category(1L, "test-category", "000000", "test-image.url", "");
        categories.save(current);
        var update = new Category(1L, "test-category", "######", "test-image.url", "");
        categories.save(update);
        assertThat(categories.findById(1L)
            .orElseThrow(() -> new RepositoryException(ErrorCode.CATEGORY_NOT_FOUND, 1L))
            .getColor()).isEqualTo("######");
    }

    @Test
    void delete() {
        var expected = new Category(1L, "test-category", "000000", "test-image.url", "");
        categories.save(expected);
        categories.deleteById(1L);
        assertThat(categories.findAll()).isEmpty();
    }
}
