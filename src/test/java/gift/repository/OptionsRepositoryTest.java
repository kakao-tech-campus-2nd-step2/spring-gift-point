package gift.repository;


import gift.exception.option.NotFoundOptionsException;
import gift.exception.product.NotFoundProductException;
import gift.model.Category;
import gift.model.Options;
import gift.model.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
public class OptionsRepositoryTest {

    @Autowired
    OptionsRepository optionsRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;

    Product savedProduct;
    Options savedOptions;
    Category savedCategory;


    @BeforeEach
    void setUp() {
        Category category = new Category("카테고리");
        savedCategory = categoryRepository.save(category);
        Product product = new Product("상품", 1000, "http://image.com", savedCategory);
        savedProduct = productRepository.save(product);
        Options options = new Options("옵션", 10, savedProduct);
        savedOptions = optionsRepository.save(options);
    }



    @DisplayName("옵션 수량 차감 테스트")
    @Test
    @Transactional
    void subtractQuantity() {
        Integer originQuantity = savedOptions.getQuantity();
        Integer subQuantity = 6;

        productRepository.findById(savedProduct.getId())
            .orElseThrow(NotFoundProductException::new);

        optionsRepository.findByIdForUpdate(savedOptions.getId())
            .ifPresentOrElse(options ->
                    options.subtractQuantity(subQuantity),
                () -> {
                    throw new NotFoundOptionsException();
                });

        // then
        Assertions.assertThat(optionsRepository.findById(savedOptions.getId())
            .get().getQuantity()).isEqualTo(originQuantity - subQuantity);
    }


}
