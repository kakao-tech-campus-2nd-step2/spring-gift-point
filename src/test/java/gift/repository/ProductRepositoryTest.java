package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.category.entity.Category;
import gift.category.repository.CategoryRepository;
import gift.product.entity.Option;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.wish.repository.WishRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class ProductRepositoryTest {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private WishRepository wishRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @BeforeEach
  public void setUp() {
    wishRepository.deleteAll();
    productRepository.deleteAll();
    categoryRepository.deleteAll();
  }

  private Category createAndSaveCategory(String name) {
    Category category = new Category();
    category.setName(name);
    return categoryRepository.save(category);
  }

  private Product createAndSaveProduct(String name, int price, String imageUrl, Category category) {
    Product product = new Product();
    product.setName(name);
    product.setPrice(price);
    product.setImageUrl(imageUrl);
    product.setCategory(category);

    Option defaultOption = new Option();
    defaultOption.setName("Default");
    defaultOption.setQuantity(1);
    defaultOption.setProduct(product);
    product.addOption(defaultOption);

    return productRepository.save(product);
  }

  private Option createAndAddOption(String name, int quantity, Product product) {
    Option option = new Option();
    option.setName(name);
    option.setQuantity(quantity);
    option.setProduct(product);
    product.addOption(option);
    return option;
  }

  @Test
  public void testSaveAndFindProduct() {
    // given
    Category category = createAndSaveCategory("Beverages");
    Product product = createAndSaveProduct("딸기 아사이", 5900, "https://example.com/image.jpg", category);
    createAndAddOption("휘핑 크림", 1, product);
    productRepository.save(product);

    // when
    Optional<Product> foundProduct = productRepository.findById(product.getId());

    // then
    assertThat(foundProduct).isPresent();
    foundProduct.ifPresent(p -> {
      assertThat(p.getName()).isEqualTo("딸기 아사이");
      assertThat(p.getPrice()).isEqualTo(5900);
      assertThat(p.getImageUrl()).isEqualTo("https://example.com/image.jpg");
      assertThat(p.getCategory().getName()).isEqualTo("Beverages");
      assertThat(p.getOptions()).hasSize(2);
      assertThat(p.getOptions().get(0).getName()).isEqualTo("Default");
      assertThat(p.getOptions().get(0).getQuantity()).isEqualTo(1);
      assertThat(p.getOptions().get(1).getName()).isEqualTo("휘핑 크림");
      assertThat(p.getOptions().get(1).getQuantity()).isEqualTo(1);
    });
  }

  @Test
  public void testUpdateProduct() {
    // given
    Category category = createAndSaveCategory("Beverages");
    Product product = createAndSaveProduct("딸기 아사이", 5900, "https://example.com/image.jpg", category);
    createAndAddOption("Large", 100, product);
    productRepository.save(product);


    // when
    product.setName("바나나 스무디");
    Option updatedOption = product.getOptions().get(1);  // Default 옵션이 0번째이므로 1번째 옵션을 수정
    updatedOption.setName("Medium");
    updatedOption.setQuantity(150);
    productRepository.save(product);

    // then
    Optional<Product> foundProduct = productRepository.findById(product.getId());
    assertThat(foundProduct).isPresent();
    foundProduct.ifPresent(p -> {
      assertThat(p.getName()).isEqualTo("바나나 스무디");
      assertThat(p.getCategory().getName()).isEqualTo("Beverages");
      assertThat(p.getOptions()).hasSize(2);
      assertThat(p.getOptions().get(0).getName()).isEqualTo("Default");
      assertThat(p.getOptions().get(0).getQuantity()).isEqualTo(1);
      assertThat(p.getOptions().get(1).getName()).isEqualTo("Medium");
      assertThat(p.getOptions().get(1).getQuantity()).isEqualTo(150);
    });
  }

  @Test
  public void testDeleteProduct() {
    // given
    Category category = createAndSaveCategory("Beverages");
    Product product = createAndSaveProduct("딸기 아사이", 5900, "https://example.com/image.jpg", category);
    Long productId = product.getId();

    // when
    wishRepository.deleteAllByProductId(productId);
    productRepository.deleteById(productId);

    // then
    Optional<Product> foundProduct = productRepository.findById(productId);
    assertThat(foundProduct).isNotPresent();
  }

  @Test
  public void testFindAllWithPagination() {
    // given
    Category category = createAndSaveCategory("Beverages");
    for (int i = 1; i <= 20; i++) {
      Product product = createAndSaveProduct("Product " + i, 1000 + i, "http://example.com/image" + i + ".jpg", category);
      createAndAddOption("Size " + i, i * 10, product);
      productRepository.save(product);
    }

    // when
    Pageable firstPageable = PageRequest.of(0, 10);
    Page<Product> firstProductPage = productRepository.findAll(firstPageable);

    // then
    assertThat(firstProductPage.getContent()).hasSize(10);
    assertThat(firstProductPage.getTotalElements()).isEqualTo(20);
    assertThat(firstProductPage.getTotalPages()).isEqualTo(2);

    // when
    Pageable secondPageable = PageRequest.of(1, 10);
    Page<Product> secondProductPage = productRepository.findAll(secondPageable);

    // then
    assertThat(secondProductPage.getContent()).hasSize(10);
    assertThat(secondProductPage.getTotalElements()).isEqualTo(20);
    assertThat(secondProductPage.getTotalPages()).isEqualTo(2);
  }
}