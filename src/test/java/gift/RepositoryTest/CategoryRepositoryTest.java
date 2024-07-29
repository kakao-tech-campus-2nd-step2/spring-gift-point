package gift.RepositoryTest;

import gift.Model.Entity.Category;
import gift.Model.Value.Name;
import gift.Repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void saveTest(){
        Category category = new Category("카테고리", "#812f3D", "카테고리 url", "카테고리 description");
        assertThat(category.getId()).isNull();
        categoryRepository.save(category);
        assertThat(category.getId()).isNotNull();
    }

    @Test
    void findByIdTest(){
        Category cateory = categoryRepository.save(new Category("카테고리", "#812f3D", "카테고리 url", "카테고리 description"));
        Optional<Category> actual = categoryRepository.findById(cateory.getId());
        assertThat(actual.get().getName().getValue()).isEqualTo("카테고리");
    }

    @Test
    void findByName(){
        categoryRepository.save(new Category("카테고리", "#812f3D", "카테고리 url", "카테고리 description"));
        Optional<Category> actual = categoryRepository.findByName(new Name("카테고리"));
        assertThat(actual).isPresent();
    }

    @Test
    void updateTest(){
       categoryRepository.save(new Category("카테고리", "#812f3D", "카테고리 url", "카테고리 description"));
        Optional<Category> optionalCategory = categoryRepository.findByName(new Name("카테고리"));
        Category category = optionalCategory.get();
        category.update("카테고리2", "#732d2b", "카테고리2 url", "카테고리2 description");

        Optional<Category> actualOptionalCategory=  categoryRepository.findById(category.getId());
        Category actual = actualOptionalCategory.get();
        assertAll(
                () -> assertThat(actual.getName().getValue()).isEqualTo("카테고리2"),
                () -> assertThat(actual.getColor().getValue()).isEqualTo("#732d2b"),
                () -> assertThat(actual.getImageUrl().getValue()).isEqualTo("카테고리2 url"),
                () -> assertThat(actual.getDescription().getValue()).isEqualTo("카테고리2 description")
        );
    }

    @Test
    void deleteTest(){
        Category category = categoryRepository.save(new Category("카테고리", "#812f3D", "카테고리 url", "카테고리 description"));
        categoryRepository.deleteById(category.getId());
        Optional<Category> actual= categoryRepository.findByName(new Name("카테고리"));
        assertThat(actual).isEmpty();
    }

}
