package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gift.model.categories.Category;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private final String testName1 = "향수";
    private final String testUrl = "imgUrl";
    private final Category testCategory = new Category(testName1, testUrl);


    @DisplayName("카테고리 추가 성공 테스트")
    @Test
    void testInsertCategorySuccess() {
        categoryRepository.save(testCategory);
        Category result = categoryRepository.findByName(testCategory.getName()).get();
        assertThat(result).usingRecursiveComparison().isEqualTo(testCategory);
    }

    @DisplayName("카테고리 추가 실패 테스트(중복 이름 uk)")
    @Test
    void testInsertDuplicateCategoryName() {
        categoryRepository.save(testCategory);
        assertThatExceptionOfType(DataIntegrityViolationException.class).isThrownBy(
            () -> categoryRepository.save(new Category(testName1, testUrl)));
    }

    @DisplayName("카테고리 목록 조회 테스트")
    @Test
    void testFindCategories() {
        String testName2 = "꽃";
        String testName3 = "김치";
        categoryRepository.save(testCategory);
        categoryRepository.save(new Category(testName2, testUrl));
        categoryRepository.save(new Category(testName3, testUrl));
        List<Category> result = categoryRepository.findAll();
        assertThat(result).extracting("name").contains(testName1, testName2, testName3);
    }

}
