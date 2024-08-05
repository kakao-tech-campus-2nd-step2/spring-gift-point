package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        Category category = categoryRepository.save(
            new Category("testName", "testColor", "testImage", "testDescription"));
        Product product = new Product("testName", 1000, "testImage.jpg", category);
        Option expected = new Option("testOption", 300, product);
        Option actual = optionRepository.save(expected);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    @Test
    @DisplayName("DB에 저장된 id를 기반으로 객체를 불러오는지 테스트")
    void findByIdTest() {
        Long First_Option_Id = 1L;
        Optional<Option> option = optionRepository.findById(First_Option_Id);

        assertThat(option).isPresent().hasValueSatisfying(
            w -> assertThat(w.getName()).isEqualTo("모바일 상품권 50,000원"));
    }

    @Test
    @DisplayName("DB를 기반으로 Product에 해당하는 옵션을 불러오는지 테스트")
    void findByProdcutIdtest() {
        Long First_Product_Id = 1L;
        List<Option> options = optionRepository.findAllByProductId(First_Product_Id);

        assertThat(options).isNotNull();
        assertThat(options).hasSize(2);
        assertThat(options.get(0).getName()).isEqualTo("모바일 상품권 50,000원");
    }
}
