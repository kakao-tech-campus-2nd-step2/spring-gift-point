package gift.repository;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.service.CategoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OptionRepository optionRepository;
    private Category category;

    @BeforeEach
    void init(){
        category = new Category("물품");
        categoryRepository.save(category);

        Product product1 = new Product("name1", 1, "url1", category);
        productRepository.save(product1);

        Product product2 = new Product("name2", 2, "url2", category);
        productRepository.save(product2);

        Product product3 = new Product("name3", 3, "url3", category);
        productRepository.save(product3);

        Product product4 = new Product("name4", 4, "url4", category);
        productRepository.save(product4);

        Product product5 = new Product("name5", 5, "url5", category);
        productRepository.save(product5);
    }

    @Test
    void saveTest(){
        // when
        Product product = new Product("name", 4500, "url", category);
        Product actual = productRepository.save(product);
        Option option = new Option("신규", 3L);
        actual.addOption(option);
      
        optionRepository.save(option);
        // then
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getName()).isEqualTo(product.getName());
    }

    @Test
    void findAllTest(){
        // when
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> products = productRepository.findAll(pageable);
        Long count = products.get().count();
        // then
        Assertions.assertThat(products).isNotNull();
        Assertions.assertThat(count).isEqualTo(5);
    }

    @Test
    void findByIdTest(){
        // when
        Product actual = productRepository.findById(1L).orElseThrow();
        Product product1 = new Product("name1", 1, "url1", category);
        // then
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getName()).isEqualTo(product1.getName());
        Assertions.assertThat(actual.getPrice()).isEqualTo(product1.getPrice());
        Assertions.assertThat(actual.getImageUrl()).isEqualTo(product1.getImageUrl());
        Assertions.assertThat(actual.getCategory()).isEqualTo(product1.getCategory());
    }

    @Test
    void deleteTest(){
        // given
        Product product = new Product("name", 4500, "url", category);
        Product actual = productRepository.save(product);
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getId()).isEqualTo(6L);
        // when
        productRepository.deleteById(6L);
        // then
        boolean tf = productRepository.existsById(6L);
        Assertions.assertThat(tf).isEqualTo(false);
    }
}