package gift.product;

import static gift.util.Utils.DEFAULT_PAGE_SIZE;
import static gift.util.Utils.TUPLE_PRODUCT_KEY;
import static gift.util.Utils.TUPLE_WISH_COUNT_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gift.category.model.CategoryRepository;
import gift.category.model.dto.Category;
import gift.product.model.ProductRepository;
import gift.product.model.dto.option.Option;
import gift.product.model.dto.product.Product;
import gift.user.model.UserRepository;
import gift.user.model.dto.AppUser;
import gift.user.model.dto.Role;
import gift.wishlist.model.WishListRepository;
import gift.wishlist.model.dto.Wish;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WishListRepository wishListRepository;
    private AppUser appUser;
    private Product product;
    private Wish wish;
    private Category category;
    private List<Option> options;

    @BeforeEach
    public void setUp() {
        appUser = new AppUser("aabb@kakao.com", "1234", Role.USER, "aaaa");
        category = new Category("기타", "");
        options = List.of(new Option("Option 1", 10, 200, product));
        product = new Product("Test", 1000, "url", appUser, category);
        wish = new Wish(appUser, product, 5);
        appUser = userRepository.save(appUser);
        category = categoryRepository.save(category);
        product = productRepository.save(product);
        wishListRepository.save(wish);
    }

    @Test
    public void testFindActiveProductById() {
        Product foundProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product"));
        assertEquals(product.getName(), foundProduct.getName());
    }

    @Test
    public void testSaveAndDelete() {
        Product savedProduct = productRepository.save(product);

        productRepository.delete(savedProduct);
        Optional<Product> result = productRepository.findById(savedProduct.getId());
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindProductWithWishCount() {
        Optional<Tuple> optionalResult = productRepository.findProductByIdWithWishCount(product.getId());

        assertTrue(optionalResult.isPresent());
        Tuple result = optionalResult.get();

        Product foundProduct = result.get(TUPLE_PRODUCT_KEY, Product.class);
        Long wishCount = result.get(TUPLE_WISH_COUNT_KEY, Long.class);

        assertEquals(product.getName(), foundProduct.getName());
        assertEquals(1L, wishCount);
    }

    @Test
    public void testFindActiveProductsByCategoryWithWishCount() {
        Pageable pageable = PageRequest.of(0, DEFAULT_PAGE_SIZE);
        Page<Tuple> resultPage = productRepository.findActiveProductsByCategoryWithWishCount(category.getId(),
                pageable);

        assertEquals(1, resultPage.getTotalElements());
        Tuple result = resultPage.getContent().get(0);
        Product foundProduct = result.get(TUPLE_PRODUCT_KEY, Product.class);
        Long wishCount = result.get(TUPLE_WISH_COUNT_KEY, Long.class);

        assertEquals(product.getName(), foundProduct.getName());
        assertEquals(Long.valueOf(1), wishCount);
    }
}
