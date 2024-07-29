package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.category.entity.Category;
import gift.category.repository.CategoryRepository;
import gift.product.entity.Product;
import gift.product.entity.Option;
import gift.product.repository.ProductRepository;
import gift.user.entity.User;
import gift.user.repository.UserRepository;
import gift.wish.entity.Wish;
import gift.wish.repository.WishRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class WishRepositoryTest {

  @Autowired
  private WishRepository wishRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @BeforeEach
  public void setUp() {
    wishRepository.deleteAll();
    userRepository.deleteAll();
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

    // 기본 옵션 추가
    Option defaultOption = new Option();
    defaultOption.setName("Large");
    defaultOption.setQuantity(1);
    defaultOption.setProduct(product);
    product.addOption(defaultOption);

    return productRepository.save(product);
  }

  private User createAndSaveUser(String email, String password) {
    User user = new User();
    user.setEmail(email);
    user.setPassword(password);
    return userRepository.save(user);
  }


  private Wish createAndSaveWish(User user, Product product) {
    Wish wish = new Wish();
    wish.setUser(user);
    wish.setProduct(product);
    return wishRepository.save(wish);
  }

  @Test
  public void testSaveAndFindById() {
    // given
    Category category = createAndSaveCategory("Beverages");
    User user = createAndSaveUser("test@example.com", "password");
    Product product = createAndSaveProduct("Test Product", 100, "http://example.com/image.jpg", category);
    Wish wish = createAndSaveWish(user, product);

    // when
    Optional<Wish> foundWish = wishRepository.findById(wish.getId());

    // then
    assertThat(foundWish).isPresent();
    assertThat(foundWish.get().getUser().getEmail()).isEqualTo("test@example.com");
    assertThat(foundWish.get().getProduct().getId()).isEqualTo(product.getId());
    assertThat(foundWish.get().getProduct().getOptions()).hasSize(1);
    assertThat(foundWish.get().getProduct().getOptions().get(0).getName()).isEqualTo("Large");
  }

  @Test
  public void testFindByUserId() {
    // given
    Category category = createAndSaveCategory("Beverages");
    User user = createAndSaveUser("test@example.com", "password");
    Product product1 = createAndSaveProduct("Test Product 1", 100, "http://example.com/image1.jpg", category);
    Product product2 = createAndSaveProduct("Test Product 2", 200, "http://example.com/image2.jpg", category);
    createAndSaveWish(user, product1);
    createAndSaveWish(user, product2);

    // when
    List<Wish> wishes = wishRepository.findByUserId(user.getId());

    // then
    assertThat(wishes).hasSize(2);
    wishes.forEach(wish -> {
      assertThat(wish.getProduct().getOptions()).hasSize(1);
      assertThat(wish.getProduct().getOptions().get(0).getName()).isEqualTo("Large");
    });
  }

  @Test
  public void testFindByProductId() {
    // given
    Category category = createAndSaveCategory("Beverages");
    User user = createAndSaveUser("test2@example.com", "password");
    Product product = createAndSaveProduct("Test Product", 100, "http://example.com/image.jpg", category);
    createAndSaveWish(user, product);

    // when
    List<Wish> wishes = wishRepository.findByProductId(product.getId());

    // then
    assertThat(wishes).hasSize(1);
    assertThat(wishes.get(0).getProduct().getOptions()).hasSize(1);
    assertThat(wishes.get(0).getProduct().getOptions().get(0).getName()).isEqualTo("Large");
  }

  @Test
  public void testDeleteWish() {
    // given
    Category category = createAndSaveCategory("Beverages");
    User user = createAndSaveUser("delete@example.com", "password");
    Product product = createAndSaveProduct("Test Product", 100, "http://example.com/image.jpg", category);
    Wish wish = createAndSaveWish(user, product);
    Long wishId = wish.getId();

    // when
    wishRepository.deleteById(wishId);
    Optional<Wish> deletedWish = wishRepository.findById(wishId);

    // then
    assertThat(deletedWish).isNotPresent();
  }

  @Test
  public void testFindByUserIdWithPagination() {
    // given
    Category category = createAndSaveCategory("Beverages");
    User user = createAndSaveUser("pagination@example.com", "password");
    for (int i = 1; i <= 15; i++) {
      Product product = createAndSaveProduct("Test Product " + i, 100 * i, "http://example.com/image" + i + ".jpg", category);
      createAndSaveWish(user, product);
    }

    // when
    Pageable pageable = PageRequest.of(0, 10);
    Page<Wish> wishesPage = wishRepository.findByUserId(user.getId(), pageable);

    // then
    assertThat(wishesPage.getContent()).hasSize(10);
    assertThat(wishesPage.getTotalElements()).isEqualTo(15);
    assertThat(wishesPage.getTotalPages()).isEqualTo(2);
    wishesPage.getContent().forEach(wish -> {
      assertThat(wish.getProduct().getOptions()).hasSize(1);
      assertThat(wish.getProduct().getOptions().get(0).getName()).isEqualTo("Large");
    });
  }
}