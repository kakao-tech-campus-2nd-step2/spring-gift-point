package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import gift.administrator.category.Category;
import gift.administrator.category.CategoryRepository;
import gift.administrator.option.Option;
import gift.administrator.option.OptionRepository;
import gift.administrator.product.Product;
import gift.administrator.product.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    private Category category;
    private Option option;
    private List<Option> options;
    private Product product;

    @BeforeEach
    void beforeEach() {
        category = new Category("상품권", null, null, null);
        categoryRepository.save(category);
        option = new Option("L", 3, null);
        options = new ArrayList<>(List.of(option));
        product = new Product("라이언", 1000, "image.jpg", category, options);
        option.setProduct(product);
        option = optionRepository.save(option);
    }

    @Test
    @DisplayName("전부 찾기")
    void findAll() {
        //given
        Option expected = new Option("L", 3, product);
        Option option1 = new Option("XL", 3, null);
        options.add(option1);
        product.setOption(options);
        productRepository.save(product);
        option1.setProduct(product);
        optionRepository.save(option1);
        Option expected1 = new Option("XL", 3, product);

        //when
        List<Option> actual = optionRepository.findAll();

        //then
        assertThat(actual).hasSize(2);
        assertThat(actual.getFirst().getId()).isNotNull();
        assertThat(actual.get(1).getId()).isNotNull();
        assertThat(actual)
            .extracting(Option::getName, Option::getQuantity, Option::getProduct)
            .containsExactly(
                tuple(expected.getName(), expected.getQuantity(), expected.getProduct()),
                tuple(expected1.getName(), expected1.getQuantity(), expected1.getProduct()));
    }

    @Test
    @DisplayName("옵션 아이디로 전부 찾기")
    void findAllById() {
        //given
        Option expected = new Option("L", 3, product);
        Option option1 = new Option("XL", 3, null);
        options.add(option1);
        product.setOption(options);
        productRepository.save(product);
        option1.setProduct(product);
        option1 = optionRepository.save(option1);
        Option expected1 = new Option("XL", 3, product);
        List<Long> list = new ArrayList<>(List.of(option.getId(), option1.getId()));

        //when
        List<Option> actual = optionRepository.findAllById(list);

        //then
        assertThat(actual).hasSize(2);
        assertThat(actual.getFirst().getId()).isNotNull();
        assertThat(actual.get(1).getId()).isNotNull();
        assertThat(actual)
            .extracting(Option::getName, Option::getQuantity, Option::getProduct)
            .containsExactly(
                tuple(expected.getName(), expected.getQuantity(), expected.getProduct()),
                tuple(expected1.getName(), expected1.getQuantity(), expected1.getProduct()));
    }

    @Test
    @DisplayName("상품 아이디로 전부 찾기")
    void findAllByProductId() {
        //given
        Option expected = new Option("L", 3, product);
        Option option1 = new Option("XL", 3, null);
        options.add(option1);
        product.setOption(options);
        product = productRepository.save(product);
        option1.setProduct(product);
        optionRepository.save(option1);
        Option expected1 = new Option("XL", 3, product);

        //when
        List<Option> actual = optionRepository.findAllByProductId(product.getId());

        //then
        assertThat(actual).hasSize(2);
        assertThat(actual.getFirst().getId()).isNotNull();
        assertThat(actual.get(1).getId()).isNotNull();
        assertThat(actual)
            .extracting(Option::getName, Option::getQuantity, Option::getProduct)
            .containsExactly(
                tuple(expected.getName(), expected.getQuantity(), expected.getProduct()),
                tuple(expected1.getName(), expected1.getQuantity(), expected1.getProduct()));
    }

    @Test
    @DisplayName("옵션 아이디로 하나 찾기")
    void findById() {
        //given
        Option expected = new Option("L", 3, product);

        //when
        Optional<Option> actual = optionRepository.findById(option.getId());

        //then
        assertThat(actual).isPresent();
        assertThat(actual.get())
            .extracting(Option::getName, Option::getQuantity, Option::getProduct)
            .containsExactly(expected.getName(), expected.getQuantity(), expected.getProduct());
    }

    @Test
    @DisplayName("옵션 아이디로 하나 찾을 때 아이디 존재하지 않음")
    void findByIdNotFound() {
        //given

        //when
        Optional<Option> actual = optionRepository.findById(option.getId() + 1);

        //then
        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("옵션 저장하기")
    void save() {
        //given
        Option expected = new Option("L", 3, product);

        //when
        Option actual = optionRepository.save(option);

        //then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual)
            .extracting(Option::getName, Option::getQuantity, Option::getProduct)
            .containsExactly(expected.getName(), expected.getQuantity(), expected.getProduct());
    }

    @Test
    @DisplayName("옵션 아이디로 삭제하기")
    void deleteById() {
        //given
        product = productRepository.save(product);

        //when
        optionRepository.deleteById(option.getId());
        Optional<Option> actual = optionRepository.findById(option.getId());

        //then
        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("상품 아이디와 옵션 아이디로 존재 여부 확인 시 존재할 때")
    void existsByIdAndProductId() {
        //given
        product = productRepository.save(product);

        //when
        boolean actual = optionRepository.existsByIdAndProductId(option.getId(), product.getId());

        //then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("상품 아이디와 옵션 아이디로 존재 여부 확인시 존재하지 않을 때")
    void existsByIdAndProductIdButNotExists() {
        //given
        product = productRepository.save(product);
        Option option1 = new Option("L", 3, null);
        List<Option> options1 = new ArrayList<>(List.of(option1));
        Product product1 = new Product("이춘식", 1000, "image.jpg", category, options1);
        option1.setProduct(product1);
        optionRepository.save(option1);
        productRepository.save(product1);

        //when
        boolean actual = optionRepository.existsByIdAndProductId(option1.getId(), product.getId());

        //then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("옵션 이름과 상품 아이디로 존재하지만 옵션 아이디는 존재하지 않음")
    void existsByNameAndProductIdAndIdNot() {
        //given
        product = productRepository.save(product);

        //when
        boolean actual = optionRepository.existsByNameAndProductIdAndIdNot(option.getName(),
            product.getId(), option.getId() + 1);

        //then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("옵션 이름과 상품 아이디로 존재하지 않거나 옵션 아이디가 존재함")
    void existsByNameAndProductIdAndIdNotReturnsFalse() {
        //given
        product = productRepository.save(product);

        //when
        boolean actual = optionRepository.existsByNameAndProductIdAndIdNot(option.getName(),
            product.getId(), option.getId());

        //then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("상품 아이디로 개수 전부 세기")
    void countAllByProductId() {
        //given
        product = productRepository.save(product);
        Option option1 = new Option("XL", 3, null);
        option1.setProduct(product);
        optionRepository.save(option1);

        //when
        int actual = optionRepository.countAllByProductId(product.getId());

        //then
        assertThat(actual).isEqualTo(2);
    }
}
