package gift.service;

import static org.assertj.core.api.Assertions.assertThat;

import gift.model.categories.CategoryDTO;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    private final String testName1 = "향수";
    private final String testName2 = "김치";
    private final String testName3 = "전자기기";
    private final String testUrl = "imgUrl";
    private final CategoryDTO testCategory1 = new CategoryDTO(0L, testName1, testUrl);
    private final CategoryDTO testCategory2 = new CategoryDTO(1L, testName2, testUrl);
    private final CategoryDTO testCategory3 = new CategoryDTO(2L, testName3, testUrl);

    @DisplayName("insertCategory 성공 테스트")
    @Test
    void testInsertCategorySuccess() {
        CategoryDTO result = categoryService.insertCategory(testCategory1);
        assertThat(result).usingRecursiveComparison().isEqualTo(testCategory1);
    }
    @Test
    void tmp(){

    }
    @DisplayName("getCategoryList 테스트")
    @Test
    void testGetCategoryList() {

        categoryService.insertCategory(testCategory1);
        categoryService.insertCategory(testCategory2);
        categoryService.insertCategory(testCategory3);
        List<CategoryDTO> result = categoryService.getCategoryList();
        assertThat(result).extracting("name").contains(testName1, testName2, testName3);
    }

    @DisplayName("updateCategory 테스트")
    @Test
    void testUpdateCategory() {
        CategoryDTO category = categoryService.insertCategory(testCategory1);
        CategoryDTO updated = new CategoryDTO(category.getId(), testName2, testUrl);
        CategoryDTO result = categoryService.updateCategory(updated);
        assertThat(result).usingRecursiveComparison().isEqualTo(updated);
    }

    @DisplayName("isDuplicateName 테스트(true)")
    @Test
    void testIsDuplicateName() {
        categoryService.insertCategory(testCategory1);
        boolean result = categoryService.isDuplicateName(testCategory1.getName());
        assertThat(result).isTrue();
    }

    @DisplayName("isDuplicateName 테스트(false)")
    @Test
    void testIsDuplicateNameFalse() {
        categoryService.insertCategory(testCategory1);
        boolean result = categoryService.isDuplicateName(testName2);
        assertThat(result).isFalse();
    }
}
