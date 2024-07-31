package gift.repository;

import gift.model.product.Category;
import gift.model.product.Option;
import gift.model.product.Product;
import gift.model.product.ProductName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save(){
        Product product = new Product(new Category("category1"),new ProductName("product1"),1000,"qwer.com");
        Option option = new Option(product,"option1",1234);
        Option option1 = optionRepository.save(option);
        assertAll(
                () -> assertThat(option1.getId()).isNotNull(),
                () -> assertThat(option1.getName()).isEqualTo(option.getName())
        );
    }
}