package gift.repository;

import gift.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CategoryRepositoryTest {

    private Category category1, category2;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        category1 = new Category("밥솥", "검은색", "압력밥솥", "www");
        category2 = new Category("밥솥", "빨간색", "압력밥솥", "www");
    }


    @Test
    void testFindByName() {
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        List<Category> foundCategories = categoryRepository.findByName("밥솥");

        // Then
        assertThat(foundCategories).isNotEmpty();
        assertThat(foundCategories.size()).isEqualTo(2);
        assertThat(foundCategories).extracting(Category::getName)
                .containsOnly("밥솥");
    }
}
