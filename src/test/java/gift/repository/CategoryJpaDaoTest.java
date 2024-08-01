package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.dto.CategoryDto;
import gift.entity.Category;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("/sql/truncateIdentity.sql")
class CategoryJpaDaoTest {

    @Autowired
    private CategoryJpaDao categoryJpaDao;
    private final Category category = new Category("음식", "Red", "http", "description");

    Category saveCategory() {
        return categoryJpaDao.save(category);
    }

    @Test
    @DisplayName("카테고리 저장 테스트")
    void save() {
        Category newCategory = saveCategory();
        assertThat(newCategory).isNotNull();
    }

    @Test
    @DisplayName("이름으로 카테고리 조회 테스트")
    void findByName() {
        saveCategory();
        assertThat(categoryJpaDao.findByName("음식").get()).isSameAs(category);
    }

    @Test
    @DisplayName("ID로 카테고리 조회 테스트")
    void findById() {
        Long id = saveCategory().getId();
        assertThat(categoryJpaDao.findById(id).get()).isSameAs(category);
    }

    @Test
    @DisplayName("카테고리 수정 테스트")
    void editCategory() {
        Category newCategory = saveCategory();
        CategoryDto categoryDto = new CategoryDto(newCategory.getId(), "의약품", "Blue", "http",
            "description");
        newCategory.updateCategory(categoryDto);
        Category queriedCategory = categoryJpaDao.findById(newCategory.getId()).get();
        assertThat(queriedCategory.getName()).isEqualTo("의약품");
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    void deleteCategory() {
        Long id = saveCategory().getId();
        categoryJpaDao.deleteById(id);
        Optional<Category> queriedCategory = categoryJpaDao.findById(id);
        assertThat(queriedCategory.isPresent()).isEqualTo(false);
    }
}