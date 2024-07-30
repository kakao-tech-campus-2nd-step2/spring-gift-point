package gift.option;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.category.Category;
import gift.category.CategoryRepository;
import gift.product.Product;
import gift.product.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setCategoryRepository() {
        categoryRepository.save(new Category("교환권","쌈@뽕한 블루","www","여름"));
        categoryRepository.save(new Category("과제면제권","방학","www.com","학교"));
        categoryRepository.save(new Category("라우브","스틸더","www.show","키야"));
    }

    @BeforeEach
    void setProductRepository() {
        productRepository.save(new Product("사과",2000,"www",categoryRepository.findById(1L).get()));
        productRepository.save(new Product("앵우", 100000, "www.com", categoryRepository.findById(2L).get()));
        productRepository.save(new Product("econo", 30000, "error", categoryRepository.findById(3L).get()));
    }

    @Test
    @DisplayName("상품에 옵션 추가 테스트")
    void add() {
        Option expected = new Option("옵션1",100,productRepository.findById(1L).get());
        Option actual = optionRepository.save(expected);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("상품의 옵션 조회 테스트")
    void find() {
        Long productId = 1L;
        optionRepository.save(new Option("옵션1",100, productRepository.findById(productId).get()));
        optionRepository.save(new Option("옵션2",200, productRepository.findById(productId).get()));
        optionRepository.save(new Option("옵션3",300, productRepository.findById(productId).get()));
        List<Option> optionList = optionRepository.findAllByProductId(productId);

        assertAll(
            () -> assertThat(optionList.size()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("옵션 정보 수정 테스트")
    void update() {
        Option option = new Option("옵션1",100,productRepository.findById(1L).get());

        option.update("옵션1-1",1000);

        assertAll(
            () -> assertThat(option.getName()).isEqualTo("옵션1-1"),
            () -> assertThat(option.getQuantity()).isEqualTo(1000)
        );
    }

    @Test
    @DisplayName("옵션 삭제 테스트")
    void delete() {
        Option option = new Option("옵션1", 100, productRepository.findById(1L).get());
        optionRepository.save(option);
        optionRepository.delete(option);

        List<Option> options = optionRepository.findById(option.getId()).stream().toList();

        assertAll(
            () -> assertThat(options.size()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("옵션 수량 차감 테스트")
    void substract() {
        Option option = new Option("옵션1", 100, productRepository.findById(1L).get());

        option.substract(200);

        assertAll(
            () -> assertThat(option.getName()).isEqualTo("옵션1"),
            () -> assertThat(option.getQuantity()).isEqualTo(200)
        );
    }

}
