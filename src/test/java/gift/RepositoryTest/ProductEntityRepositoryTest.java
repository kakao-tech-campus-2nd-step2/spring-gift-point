package gift.RepositoryTest;

import gift.Model.Entity.CategoryEntity;
import gift.Model.Entity.ProductEntity;
import gift.Repository.CategoryRepository;
import gift.Repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class ProductEntityRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeTestClass
    public void setup(){
        categoryRepository.save(new CategoryEntity("test1","test3","test4"));
    }

    @Test
    void save(){
        ProductEntity expected = new ProductEntity("a", 1000, "b", categoryRepository.findById(1L).get());
        ProductEntity actual = productRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    void findByName() {
        String expectedName = "a";
        int expectedPrice = 1000;
        String expectedImageUrl = "b";
        productRepository.save(new ProductEntity("a", 1000, "b", categoryRepository.findById(1L).get()));
        String actual = productRepository.findByName(expectedName).get().getName();
        assertThat(actual).isEqualTo(expectedName);
    }
}
