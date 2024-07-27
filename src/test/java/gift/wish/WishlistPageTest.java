package gift.wish;

import gift.member.model.Member;
import gift.member.repository.MemberRepository;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import gift.wish.model.WishDTO;
import gift.wish.repository.WishRepository;
import gift.wish.service.WishService;
import gift.wish.service.WishServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(WishServiceImpl.class)
@Transactional
public class WishlistPageTest {
    @Autowired
    private WishService wishService;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    private Member testMember;

    @BeforeEach
    public void setUp() {
        // Given
        // 1.
        testMember = new Member("test@example.com", "password");
        memberRepository.save(testMember);

        Product testProduct1 = new Product("Ice Americano", 2500, "http://example.com/example.jpg");
        productRepository.save(testProduct1);
        wishService.createWish(testMember, testProduct1.getId());

        // 2.
        Product testProduct2 = new Product("Caffe Latte", 3500, "http://example.com/example.jpg");
        productRepository.save(testProduct2);
        wishService.createWish(testMember, testProduct2.getId());

        // 3.
        Product testProduct3 = new Product("Hot Americano", 2000, "http://example.com/example.jpg");
        productRepository.save(testProduct3);
        wishService.createWish(testMember, testProduct3.getId());

        // 4.
        Product testProduct4 = new Product("Honey Grapefruit Tea", 5500, "http://example.com/example.jpg");
        productRepository.save(testProduct4);
        wishService.createWish(testMember, testProduct4.getId());

        // 5.
        Product testProduct5 = new Product("Milk Shake", 5000, "http://example.com/example.jpg");
        productRepository.save(testProduct5);
        wishService.createWish(testMember, testProduct5.getId());
    }

    @Test
    public void testPagination() {
        // When
        Page<WishDTO> page1 = wishService.getWishlistByPage(0, 2, "price", "desc");
        Page<WishDTO> page2 = wishService.getWishlistByPage(1, 2, "price", "desc");

        // Then
        // Page 1 assertions
        assertThat(page1.getContent()).hasSize(2);
        assertThat(page1.getContent().get(0).getProductName()).isEqualTo("Honey Grapefruit Tea");
        assertThat(page1.getContent().get(1).getProductName()).isEqualTo("Milk Shake");

        // Page 2 assertions
        assertThat(page2.getContent()).hasSize(2);
        assertThat(page2.getContent().get(0).getProductName()).isEqualTo("Caffe Latte");
        assertThat(page2.getContent().get(1).getProductName()).isEqualTo("Ice Americano");

        // Additional assertions for total pages and elements
        assertThat(page1.getTotalPages()).isEqualTo(3);
        assertThat(page1.getTotalElements()).isEqualTo(5);
    }
}
