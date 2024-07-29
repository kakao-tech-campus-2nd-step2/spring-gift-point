package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import gift.administrator.category.Category;
import gift.administrator.category.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    private Category category;

    @BeforeEach
    void beforeEach() {
        category = new Category("상품권", "#ff11ff", "image.jpg", "money");
    }

    @Test
    @DisplayName("카테고리 추가 테스트")
    void save() {
        //Given

        //When
        Category actual = categoryRepository.save(category);
        Category expected = new Category("상품권", "#ff11ff", "image.jpg", "money");

        //Then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual)
            .extracting(Category::getName, Category::getColor, Category::getImageUrl,
                Category::getDescription)
            .containsExactly(expected.getName(), expected.getColor(), expected.getImageUrl(),
                expected.getDescription());
    }

    @Test
    @DisplayName("카테고리에 있는 이름을 다시 추가할 때 DB에서 오류")
    void saveSameNameError() {
        //Given
        categoryRepository.save(category);
        Category category1 = new Category("상품권", "#ff11ff", "image.jpg", "money");

        //When

        //Then
        assertThatThrownBy(() -> {
            categoryRepository.save(category1);
        });
    }

    @Test
    @DisplayName("카테고리 전부 찾기")
    void findAll() {
        //Given
        categoryRepository.save(category);
        Category category1 = new Category("가전", "#ddff11", "image.jpg", "");
        categoryRepository.save(category1);
        Category expected = new Category("상품권", "#ff11ff", "image.jpg", "money");
        Category expected1 = new Category("가전", "#ddff11", "image.jpg", "");

        //When
        List<Category> actual = categoryRepository.findAll();

        //Then
        assertThat(actual).hasSize(2);
        assertThat(actual.getFirst().getId()).isNotNull();
        assertThat(actual.get(1).getId()).isNotNull();
        assertThat(actual)
            .extracting(Category::getName, Category::getColor, Category::getImageUrl,
                Category::getDescription)
            .containsExactly(
                tuple(expected.getName(), expected.getColor(), expected.getImageUrl(),
                    expected.getDescription()),
                tuple(expected1.getName(), expected1.getColor(), expected1.getImageUrl(),
                    expected1.getDescription())
            );
    }

    @Test
    @DisplayName("카테고리 아이디로 찾기 테스트")
    void findById() {
        //Given
        categoryRepository.save(category);
        Category expected = new Category("상품권", "#ff11ff", "image.jpg", "money");

        //When
        Optional<Category> actual = categoryRepository.findById(category.getId());

        //Then
        assertThat(actual).isPresent();
        assertThat(actual.get())
            .extracting(Category::getName, Category::getColor, Category::getImageUrl,
                Category::getDescription)
            .containsExactly(expected.getName(), expected.getColor(), expected.getImageUrl(),
                expected.getDescription());
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    void deleteById() {
        //Given
        categoryRepository.save(category);

        //When
        categoryRepository.deleteById(category.getId());
        Optional<Category> actual = categoryRepository.findById(category.getId());

        //Then
        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("카테고리 아이디로 찾기")
    void existsById() {
        //Given
        categoryRepository.save(category);

        //When
        boolean actual = categoryRepository.existsById(category.getId());

        //Then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 아이디로 카테고리를 찾으면 false 리턴")
    void notExistsById() {
        //Given
        categoryRepository.save(category);

        //When
        boolean actual = categoryRepository.existsById(category.getId() + 1);

        //Then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("카테고리 이름으로 찾기")
    void existsByName() {
        //Given
        categoryRepository.save(category);

        //When
        boolean actual = categoryRepository.existsByName(category.getName());

        //Then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("존재하지 않는 이름으로 카테고리를 찾으면 false 리턴")
    void notExistsByName() {
        //Given
        categoryRepository.save(category);

        //When
        boolean actual = categoryRepository.existsByName("이춘식");

        //Then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("카테고리 이름은 존재하지만 카테고리 아이디는 존재하지 않음")
    void existsByNameAndIdNot() {
        //Given
        categoryRepository.save(category);

        //When
        boolean actual = categoryRepository.existsByNameAndIdNot(category.getName(), category.getId() + 1);

        //Then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("카테고리 이름이 존재하지 않거나 카테고리 아이디가 존재함")
    void existsByNameAndIdNotReturnsFalse() {
        //Given
        categoryRepository.save(category);

        //When
        boolean actual = categoryRepository.existsByNameAndIdNot(category.getName(), category.getId());

        //Then
        assertThat(actual).isEqualTo(false);
    }
}
