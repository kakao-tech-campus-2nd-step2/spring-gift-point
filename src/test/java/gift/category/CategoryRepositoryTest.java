package gift.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import gift.product.Product;
import gift.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setCategoryRepository() {
        categoryRepository.save(new Category("교환권","쌈@뽕한 블루","www","여름"));
        categoryRepository.save(new Category("과제면제권","방학","www.com","학교"));
        categoryRepository.save(new Category("라우브","스틸더","www.show","키야"));
    }

    @Test
    @DisplayName("카테고리 저장 테스트")
    void save() {
        Category expected = new Category("교환권","쌈@뽕한 블루","www","여름");
        Category actual = categoryRepository.save(expected);

        assertAll(
            () -> assertThat(actual).isEqualTo(expected)
        );
    }

    @Test
    @DisplayName("단일 카테고리 조회 테스트")
    void findById() {
        Category expected = new Category("교환권","쌈@뽕한 블루","www","여름");
        categoryRepository.save(expected);
        Category actual = categoryRepository.findById(expected.getId()).get();

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
            () -> assertThat(actual.getName()).isEqualTo("교환권"),
            () -> assertThat(actual.getColor()).isEqualTo("쌈@뽕한 블루"),
            () -> assertThat(actual.getImageUrl()).isEqualTo("www"),
            () -> assertThat(actual.getDescription()).isEqualTo("여름")
        );
    }

    @Test
    @DisplayName("모든 카테고리 조회 테스트")
    void findAll() {
        Category category1 = new Category("교환권","쌈@뽕한 블루","www","여름");
        Category category2 = new Category("과제면제권","방학","www.com","학교");
        Category category3 = new Category("라우브","스틸더","www.show","키야");
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        List<Category> categoryList = categoryRepository.findAll();

        assertAll(
            ()-> assertThat(categoryList.size()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("카테고리 수정 테스트")
    void update() {
        Category category = new Category("교환권","쌈@뽕한 블루","www","여름");

        category.update("과제면제권","방학","www.com","학교");

        assertAll(
            () -> assertThat(category.getName()).isEqualTo("과제면제권"),
            () -> assertThat(category.getColor()).isEqualTo("방학"),
            () -> assertThat(category.getImageUrl()).isEqualTo("www.com"),
            () -> assertThat(category.getDescription()).isEqualTo("학교")
        );
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    void delete() {
        Category category = new Category("교환권","쌈@뽕한 블루","www","여름");
        categoryRepository.save(category);
        categoryRepository.deleteById(category.getId());

        List<Category> isCategory = categoryRepository.findById(category.getId()).stream().toList();

        assertAll(
            () -> assertThat(isCategory.size()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("카테고리 속 상품 조회 테스트")
    void findProductInCategory() {
        Category category = new Category("교환권","쌈@뽕한 블루","www","여름");
        categoryRepository.save(category);
        productRepository.save(new Product("사과", 2000, "www", category));
        productRepository.save(new Product("참외",4000,"달다!", category));

        List<Product> productList = productRepository.findAllByCategory_Id(category.getId());

        assertAll(
            () -> assertThat(productList.size()).isEqualTo(2)
        );
    }

}
