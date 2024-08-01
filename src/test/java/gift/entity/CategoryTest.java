package gift.entity;

import gift.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
public class CategoryTest {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TestEntityManager entityManager;
    private Category testCategory;

    @BeforeEach
    public void setUp() {
        testCategory = new Category("test", "test", "test", "test");
        categoryRepository.save(testCategory);
    }

    @AfterEach
    public void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    void testFindAll() {
        List<Category> foundedCategories = categoryRepository.findAll();
        assertEquals(testCategory.getId(), foundedCategories.get(0).getId());
    }

    @Test
    void testSaveCategory() {
        Category foundCategory = categoryRepository.findById(testCategory.getId()).get();
        assertEquals(testCategory.getId(), foundCategory.getId());
    }

    @Test
    void testUpdateCategory() {
        var updatedCategory = new Category(1, "updated", "updated", "updated", "updated");
        categoryRepository.save(updatedCategory);
        Category foundCategory = categoryRepository.findById(testCategory.getId()).get();
        assertEquals(testCategory.getName(), foundCategory.getName());
    }
}
