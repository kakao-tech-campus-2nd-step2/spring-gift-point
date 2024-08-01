package gift;

import gift.model.Category;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wishlist;
import gift.repository.CategoryRepository;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class WishlistRepositoryTest {

  @Autowired
  private WishlistRepository wishlistRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private CategoryRepository categoryRepository;

  private Member member;
  private Product product;
  private Category category;

  @BeforeEach
  public void setUp() {
    category = createAndSaveCategory("Test Category", "Blue", "http://example.com/category");
    member = createAndSaveMember("test@example.com", "password", "Test User");
    product = createAndSaveProduct("Test Product", 100, "http://example.com/product");
  }

  @Test
  void whenSaveWishlist_thenWishlistIsPersisted() {
    Wishlist wishlist = createAndSaveWishlist(member, product);

    assertThat(wishlistRepository.findById(wishlist.getId())).isPresent();
  }

  @Test
  void givenMemberId_whenFindByMemberId_thenReturnsWishlistPage() {
    createAndSaveWishlist(member, product);

    var wishlists = wishlistRepository.findByMemberId(member.getId(), PageRequest.of(0, 10));

    assertThat(wishlists.getContent()).hasSize(1);
  }

  @Test
  void givenMemberId_whenFindByMemberIdWithPagination_thenCorrectPageInformation() {
    for (int i = 1; i <= 5; i++) {
      Product newProduct = createAndSaveProduct("Product " + i, 100 + i, "http://example.com/product-" + i);
      createAndSaveWishlist(member, newProduct);
    }

    var wishlistPage = wishlistRepository.findByMemberId(member.getId(), PageRequest.of(0, 3));

    assertThat(wishlistPage).hasSize(3)
            .extracting("product.name")
            .containsExactly("Product 1", "Product 2", "Product 3");
  }

  private Wishlist createAndSaveWishlist(Member member, Product product) {
    Wishlist wishlist = new Wishlist();
    wishlist.setMember(member);
    wishlist.setProduct(product);
    return wishlistRepository.save(wishlist);
  }

  private Product createAndSaveProduct(String name, int price, String imageUrl) {
    Product product = new Product(name, price, imageUrl, category);
    return productRepository.save(product);
  }
  private Member createAndSaveMember(String email, String password, String name) {
    Member member = new Member(email, password, name);
    return memberRepository.save(member);
  }
  private Category createAndSaveCategory(String name, String color, String imageUrl) {
    Category category = new Category(name, color, imageUrl, "Test Description");
    return categoryRepository.save(category);
  }
}
