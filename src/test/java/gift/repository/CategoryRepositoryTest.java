package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gift.entity.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
        categoryRepository.save(new Category("테스트1", "#000000", "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg", ""));
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }


    @Test
    void findByName() {
        assertThat(categoryRepository.findByName("테스트")).isNotNull();
    }

    @Test
    void existsByName() {
        assertTrue(categoryRepository.existsByName("테스트1"));
    }

    @Test
    void findById() {
        assertThat(categoryRepository.findById(1L)).isNotNull();
    }

    @Test
    void deleteById() {
        categoryRepository.deleteById(1L);
        assertThat(categoryRepository.findById(1L)).isEmpty();
    }
}