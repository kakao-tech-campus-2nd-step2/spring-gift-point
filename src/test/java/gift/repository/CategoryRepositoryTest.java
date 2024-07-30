package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.model.Category;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category1;
    private Category category2;
    private Category savedCategory;

    @BeforeEach
    void setUp() {
        category1 = new Category(null, "상품권");
        category2 = new Category(null, "카카오프렌즈");
        savedCategory = categoryRepository.save(category1);
        categoryRepository.save(category2);
    }

    @Test
    void testSave() {
        assertAll(
            () -> assertThat(savedCategory.getId()).isNotNull(),
            () -> assertThat(savedCategory.getName()).isEqualTo(category1.getName())
        );
    }

    @Test
    void testFindAll() {
        List<Category> categories = categoryRepository.findAll();
        assertAll(
            () -> assertThat(categories.size()).isEqualTo(4),
            () -> assertThat(categories.get(2).getName()).isEqualTo(category1.getName()),
            () -> assertThat(categories.get(3).getName()).isEqualTo(category2.getName())
        );
    }

    @Test
    void testFindById() {
        Category foundCategory = categoryRepository.findById(savedCategory.getId()).get();
        assertAll(
            () -> assertThat(foundCategory).isNotNull(),
            () -> assertThat(foundCategory.getId()).isEqualTo(savedCategory.getId())
        );
    }

    @Test
    void testFindByName() {
        Category foundCategory = categoryRepository.findByName(savedCategory.getName());
        assertAll(
            () -> assertThat(foundCategory).isNotNull(),
            () -> assertThat(foundCategory.getName()).isEqualTo(savedCategory.getName())
        );
    }

    @Test
    void testDelete() {
        categoryRepository.delete(savedCategory);
        boolean exists = categoryRepository.existsById(savedCategory.getId());
        assertThat(exists).isFalse();
    }

}