package gift.repository;

import gift.category.repository.CategoryRepository;
import gift.product.entity.Option;
import gift.product.entity.Product;
import gift.category.entity.Category;
import gift.product.repository.OptionRepository;
import gift.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OptionRepositoryTest {

  @Autowired
  private OptionRepository optionRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  private Product testProduct;

  @BeforeEach
  void setUp() {
    Category category = new Category();
    category.setName("Beverages");
    category = categoryRepository.save(category);

    testProduct = new Product();
    testProduct.setName("아메리카노");
    testProduct.setPrice(4500);
    testProduct.setImageUrl("http://example.com/americano.jpg");
    testProduct.setCategory(category);

    Option defaultOption = new Option();
    defaultOption.setName("아이스");
    defaultOption.setQuantity(1);
    testProduct.addOption(defaultOption);

    testProduct = productRepository.save(testProduct);
  }

  @Test
  void existsByProductIdAndName() {
    boolean exists = optionRepository.existsByProductIdAndName(testProduct.getId(), "아이스");

    assertThat(exists).isTrue();
  }

  @Test
  void notExistsByProductIdAndName() {
    boolean exists = optionRepository.existsByProductIdAndName(testProduct.getId(), "핫");

    assertThat(exists).isFalse();
  }

  @Test
  void findByProductId() {
    Option newOption = new Option();
    newOption.setName("샷 추가");
    newOption.setQuantity(1);
    testProduct.addOption(newOption);
    productRepository.save(testProduct);

    List<Option> options = optionRepository.findByProductId(testProduct.getId());

    assertThat(options).hasSize(2);
    assertThat(options).extracting(Option::getName).containsExactlyInAnyOrder("아이스", "샷 추가");
    assertThat(options).allSatisfy(option -> assertThat(option.getProduct()).isEqualTo(testProduct));
  }

  @Test
  void findByProductIdWithSingleOption() {
    List<Option> options = optionRepository.findByProductId(testProduct.getId());

    assertThat(options).hasSize(1);
    assertThat(options.get(0).getName()).isEqualTo("아이스");
    assertThat(options.get(0).getProduct()).isEqualTo(testProduct);
  }


  @Test
  void findAllByProduct() {
    Option newOption = new Option();
    newOption.setName("샷 추가");
    newOption.setQuantity(1);
    testProduct.addOption(newOption);
    productRepository.save(testProduct);

    List<Option> options = optionRepository.findAllByProduct(testProduct);

    assertThat(options).hasSize(2);
    assertThat(options).extracting(Option::getName).containsExactlyInAnyOrder("아이스", "샷 추가");
    assertThat(options).allSatisfy(option -> {
      assertThat(option.getProduct()).isEqualTo(testProduct);
      assertThat(option.getQuantity()).isEqualTo(1);
    });

  }
}