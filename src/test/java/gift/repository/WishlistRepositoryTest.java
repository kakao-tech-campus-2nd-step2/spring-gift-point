package gift.repository;

import gift.entity.Category;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class WishlistRepositoryTest {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);
        Member member = memberRepository.save(new Member("test@example.com", "password"));
        Product product = productRepository.save(new Product("Test Product", 100, "http://example.com/test.jpg", category));

        Wish expected = new Wish(member, product, 1);
        Wish actual = wishlistRepository.save(expected);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getMember().getId()).isEqualTo(expected.getMember().getId());
        assertThat(actual.getProduct().getId()).isEqualTo(expected.getProduct().getId());
        assertThat(actual.getProductNumber()).isEqualTo(expected.getProductNumber());
    }

    @Test
    void findByMemberIdAndProductId() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);
        Member member = memberRepository.save(new Member("test@example.com", "password"));
        Product product = productRepository.save(new Product("Test Product", 100, "http://example.com/test.jpg", category));

        wishlistRepository.save(new Wish(member, product, 1));

        Optional<Wish> actual = wishlistRepository.findByMemberIdAndProductId(member.getId(), product.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get().getProductNumber()).isEqualTo(1);
    }

    @Test
    void findByMemberId() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);
        Member member = memberRepository.save(new Member("test@example.com", "password"));
        Product product1 = productRepository.save(new Product("Product 1", 100, "http://example.com/1.jpg", category));
        Product product2 = productRepository.save(new Product("Product 2", 200, "http://example.com/2.jpg", category));

        wishlistRepository.save(new Wish(member, product1, 1));
        wishlistRepository.save(new Wish(member, product2, 2));

        List<Wish> wishes = wishlistRepository.findByMemberId(member.getId());
        assertThat(wishes).hasSize(2);
    }

    @Test
    void deleteById() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);
        Member member = memberRepository.save(new Member("test@example.com", "password"));
        Product product = productRepository.save(new Product("Test Product", 100, "http://example.com/test.jpg", category));

        Wish wish = wishlistRepository.save(new Wish(member, product, 1));
        wishlistRepository.deleteById(wish.getId());

        Optional<Wish> deletedWish = wishlistRepository.findById(wish.getId());
        assertThat(deletedWish).isNotPresent();
    }
}
