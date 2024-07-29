package gift.repository;

import gift.entity.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    public void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    public void 카테고리_저장_후_조회_성공() {
        Category category = new Category("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리");
        categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findById(category.getId());
        assertTrue(foundCategory.isPresent());
        assertEquals("테스트카테고리", foundCategory.get().getName());
        assertEquals("#FF0000", foundCategory.get().getColor());
        assertEquals("https://example.com/test.png", foundCategory.get().getImageUrl());
        assertEquals("테스트 카테고리", foundCategory.get().getDescription());
    }

    @Test
    public void 카테고리_삭제_성공() {
        Category category = new Category("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리");
        categoryRepository.save(category);

        categoryRepository.delete(category);

        Optional<Category> foundCategory = categoryRepository.findById(category.getId());
        assertFalse(foundCategory.isPresent());
    }
}
