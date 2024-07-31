package gift.repository;

import gift.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save() {
        Category expected = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        Category actual = categoryRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getColor()).isEqualTo(expected.getColor()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl()),
                () -> assertThat(actual.getDescription()).isEqualTo(expected.getDescription())
        );
    }

    @Test
    void findById() {
        Category savedCategory = categoryRepository.save(new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description"));
        Optional<Category> actual = categoryRepository.findById(savedCategory.getId());
        assertThat(actual).isPresent();
        actual.ifPresent(category -> assertThat(category.getName()).isEqualTo("Test Category"));
    }

    @Test
    void findAll() {
        categoryRepository.save(new Category("Category 1", "#FFFFFF", "http://example.com/1.jpg", "Description 1"));
        categoryRepository.save(new Category("Category 2", "#000000", "http://example.com/2.jpg", "Description 2"));
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(2);
    }

    @Test
    void deleteById() {
        Category savedCategory = categoryRepository.save(new Category("Category", "#FFFFFF", "http://example.com/delete.jpg", "Description"));
        categoryRepository.deleteById(savedCategory.getId());
        Optional<Category> deletedCategory = categoryRepository.findById(savedCategory.getId());
        assertThat(deletedCategory).isNotPresent();
    }

    @Test
    void updateCategory() {
        Category savedCategory = categoryRepository.save(new Category("Original Name", "#FFFFFF", "http://example.com/original.jpg", "Original Description"));
        savedCategory.setName("Updated Name");
        savedCategory.setColor("#000000");
        savedCategory.setImageUrl("http://example.com/updated.jpg");
        savedCategory.setDescription("Updated Description");

        Category updatedCategory = categoryRepository.save(savedCategory);

        assertAll(
                () -> assertThat(updatedCategory.getName()).isEqualTo("Updated Name"),
                () -> assertThat(updatedCategory.getColor()).isEqualTo("#000000"),
                () -> assertThat(updatedCategory.getImageUrl()).isEqualTo("http://example.com/updated.jpg"),
                () -> assertThat(updatedCategory.getDescription()).isEqualTo("Updated Description")
        );
    }
}
