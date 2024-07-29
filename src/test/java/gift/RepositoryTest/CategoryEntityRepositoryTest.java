package gift.RepositoryTest;

import gift.Model.Entity.CategoryEntity;
import gift.Repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class CategoryEntityRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save(){
        CategoryEntity expected = new CategoryEntity("testName", "testColor", "testUrl", "testDescription");
        CategoryEntity actual = categoryRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    void findByName() {
        String expectedName = "testName";
        String expectedColor = "testColor";
        String expectedImageUrl = "testImageUrl";
        String expectedDescription = "testDescription";
        categoryRepository.save(new CategoryEntity(expectedName, expectedColor, expectedImageUrl, expectedDescription));
        String actual = categoryRepository.findByName(expectedName).get().getName();
        assertThat(actual).isEqualTo(expectedName);
    }
}
