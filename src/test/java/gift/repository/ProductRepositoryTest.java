package gift.repository;


import gift.category.domain.Category;
import gift.category.repository.CategoryRepository;
import gift.product.domain.Product;
import gift.product.repository.ProductRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Test
    @DisplayName("save()")
    void save(){
        //given
        Product expected = new Product();
        expected.setName("물병");
        expected.setPrice(1000);
        expected.setImageUrl("https://www.water.com");
        categoryRepository.save(new Category("name"));
        expected.setCategory(categoryRepository.findById(1L).get());
        //when
        Product actual = productRepository.save(expected);
        //then
        Assertions.assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    @DisplayName("findById()")
    void findById(){
        //given
        Product expected = new Product();
        expected.setName("물병");
        expected.setPrice(1000);
        expected.setImageUrl("https://www.water.com");
        categoryRepository.save(new Category("name"));
        expected.setCategory(categoryRepository.findById(1L).get());

        Product savedProduct = productRepository.save(expected);
        Long expectedId = savedProduct.getId();
        //when
        Optional<Product> actualOptional = productRepository.findById(expectedId);

        //then
        Assertions.assertThat(actualOptional).isPresent();
        Product actual = actualOptional.get();
        Assertions.assertThat(actual).isEqualToComparingFieldByField(expected);
    }
}
