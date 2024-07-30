package gift.test.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import gift.entity.Category;
import gift.entity.Product;
import gift.entity.User;
import gift.entity.Wishlist;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishlistRepository;

@DataJpaTest
public class WishlistRepositoryTest {

	private final WishlistRepository wishlistRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	
	@Autowired
	public WishlistRepositoryTest(WishlistRepository wishlistRepository, UserRepository userRepository,
			ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.wishlistRepository = wishlistRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	private User user;
	private Product product;
	private Category category;

	@BeforeEach
	void setUp() {
		user = new User("tset@test.com", "pw");
		userRepository.save(user);
		category = new Category("교환권", "#6c95d1", "https://example.com/image.jpg", "");
		categoryRepository.save(category);
		product = new Product("아이스 아메리카노 T", 4500, "https://example.com/image.jpg", category);
		productRepository.save(product);
	}

	@Test
	void save() {
		Wishlist expected = new Wishlist(user, product);
		expected.setQuantity(2);
		Wishlist actual = wishlistRepository.save(expected);

		assertThat(actual.getId()).isEqualTo(expected.getId());
		assertThat(actual.getUser()).isEqualTo(expected.getUser());
		assertThat(actual.getProduct()).isEqualTo(expected.getProduct());
		assertThat(actual.getQuantity()).isEqualTo(expected.getQuantity());
	}

	@Test
	void findByUserId() {
		for (int i = 1; i <= 15; i++) {
			Product product = new Product("product " + i, 4500, "https://example.com/image.jpg", category);
			productRepository.save(product);
			Wishlist wishlist = new Wishlist(user, product);
			wishlist.setQuantity(i);
			wishlistRepository.save(wishlist);
		}

		Pageable pageable = PageRequest.of(0, 10);
		Page<Wishlist> wishlistItemsPage = wishlistRepository.findByUserId(user.getId(), pageable);

		assertThat(wishlistItemsPage.getContent()).hasSize(10);
		assertThat(wishlistItemsPage.getTotalElements()).isEqualTo(15);
		assertThat(wishlistItemsPage.getNumber()).isEqualTo(0);
	}
}
