package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.common.exception.CategoryException;
import gift.common.exception.ErrorCode;
import gift.model.Category;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("/truncate.sql")
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 등록")
    void save() {
        Category category = new Category(null, "차량", "brown", "www.aaa.jpg", "차량 카테고리입니다.");
        Category actual = categoryRepository.save(category);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(category.getName()),
            () -> assertThat(actual.getColor()).isEqualTo(category.getColor()),
            () -> assertThat(actual.getImageUrl()).isEqualTo(category.getImageUrl()),
            () -> assertThat(actual.getDescription()).isEqualTo(category.getDescription())
        );
    }

    @Test
    @DisplayName("카테고리 조회")
    void findById() {
        Category category = categoryRepository.save(
            new Category(null, "차량", "brown", "www.aaa.jpg", "차량 카테고리입니다."));
        Category actual = categoryRepository.findById(category.getId())
            .orElseThrow(() -> new CategoryException(ErrorCode.CATEGORY_NOT_FOUND));

        assertThat(actual).isEqualTo(category);
    }

    @Test
    @DisplayName("전체 카테고리 조회")
    void findAll() {
        Category category1 = categoryRepository.save(
            new Category(null, "차량", "brown", "www.aaa.jpg", "차량 카테고리입니다."));
        Category category2 = categoryRepository.save(
            new Category(null, "상품권", "brown", "www.aaa.jpg", "상품권 카테고리입니다."));

        List<Category> categories = categoryRepository.findAll();

        assertThat(categories).hasSize(2);
    }

    @Test
    @DisplayName("카테고리 수정")
    void update() {
        Category category = new Category(null, "차량", "brown", "www.aaa.jpg", "차량 카테고리입니다.");
        categoryRepository.save(category);

        category.updateCategory("상품권", "blue", "update.jpg", "상품권 카테고리입니다.");

        assertAll(
            () -> assertThat(category.getName()).isEqualTo("상품권"),
            () -> assertThat(category.getColor()).isEqualTo("blue"),
            () -> assertThat(category.getImageUrl()).isEqualTo("update.jpg"),
            () -> assertThat(category.getDescription()).isEqualTo("상품권 카테고리입니다.")
        );
    }

    @Test
    @DisplayName("카테고리 삭제")
    void delete() {
        Category category1 = new Category(null, "차량", "brown", "www.aaa.jpg", "차량 카테고리입니다.");
        Category category2 = new Category(null, "상품권", "brown", "www.aaa.jpg", "상품권 카테고리입니다.");
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        categoryRepository.deleteById(category1.getId());
        List<Category> categories = categoryRepository.findAll();

        assertThat(categories).hasSize(1);
    }
}
