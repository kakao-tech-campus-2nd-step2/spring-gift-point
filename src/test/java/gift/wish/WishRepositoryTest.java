package gift.wish;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.member.Member;
import gift.member.MemberRepository;
import gift.product.Product;
import gift.product.ProductRepository;
import gift.wishes.Wish;
import gift.wishes.WishRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@Transactional
public class WishRepositoryTest {
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private Member member;
    private Product product;

    @Autowired
    public WishRepositoryTest(WishRepository wishRepository, MemberRepository memberRepository, ProductRepository productRepository){
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }
    @BeforeEach
    void beforeEach(){
        member = new Member(null, "na", "na@gmail.com","1234");
        product = new Product(null, "test_product", 1234, null, 1L);
        memberRepository.save(member);
        productRepository.save(product);
    }

    @Test
    void save(){
        Wish expected = new Wish(member, product, 1L);
        Wish actual = wishRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getMember()).isEqualTo(expected.getMember()),
            () -> assertThat(actual.getProduct()).isEqualTo(expected.getProduct()),
            () -> assertThat(actual.getQuantity()).isEqualTo(expected.getQuantity())
        );
    }

    @Test
    void deleteByIdAndMember(){
        Wish expected = new Wish(member, product, 1L);
        wishRepository.save(expected);

        wishRepository.deleteByIdAndMember(expected.getId(), expected.getMember());
        assertThat(wishRepository.findById(expected.getId())).isEmpty();
    }
}
