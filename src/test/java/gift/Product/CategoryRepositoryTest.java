package gift.Product;

import static org.assertj.core.api.Assertions.assertThat;

import gift.model.Category;
import gift.model.Product;
import gift.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void findById() {
        Category category = new Category("교환권");
        Category expected = categoryRepository.save(category);
        Category actual = categoryRepository.findById(expected.getId()).get();
        assertThat(expected.getCategoryName()).isEqualTo(actual.getCategoryName());
    }

    @Test
    public void save() {
        Category expected = new Category("교환권");
        Category actual = categoryRepository.save(expected);
        assertThat(expected.getCategoryName()).isEqualTo(actual.getCategoryName());
    }

    @Test
    public void delete() {
        Category category = new Category("교환권");
        Category excepted = categoryRepository.save(category);
        categoryRepository.delete(excepted);
        Optional<Category> actual = categoryRepository.findById(excepted.getId());
        assertThat(actual).isNotPresent();
    }


}
