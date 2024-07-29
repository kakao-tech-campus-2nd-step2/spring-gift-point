package gift;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gift.model.Category;
import gift.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void whenCategoryIsSaved_thenCategoryIsPersistedCorrectly() {
        Category category = new Category(null, "TestCategory");
        Category savedCategory = categoryRepository.save(category);

        assertAll(
            () -> assertNotNull(savedCategory.getId()),
            () -> assertEquals("TestCategory", savedCategory.getName())
        );
    }
}