package gift.wish.repository;

import gift.member.domain.*;
import gift.member.repository.MemberRepository;
import gift.product.domain.ImageUrl;
import gift.product.domain.Product;
import gift.product.domain.ProductName;
import gift.product.domain.ProductPrice;
import gift.product.repository.ProductRepository;
import gift.wish.domain.ProductCount;
import gift.wish.domain.Wish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Member member;
    private Product product;
    private Wish wish;
    private Wish savedWish;

    @BeforeEach
    void beforeEach() {
        member = memberRepository.save(new Member(null, MemberType.USER, new Email("email"), new Password("password"), new Nickname("nickname")));
        product = productRepository.save(new Product(null, new ProductName("name"), new ProductPrice(10L), new ImageUrl("imageUrl")));
        wish = new Wish(null, member, product, new ProductCount(10L));
        savedWish = wishRepository.save(wish);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @Description("save 테스트")
    void saveTest() {
        // given
        // when
        // then
        assertThat(savedWish.getId()).isNotNull();
        assertThat(savedWish.getMember()).isEqualTo(wish.getMember());
        assertThat(savedWish.getProduct()).isEqualTo(wish.getProduct());
        assertThat(savedWish.getProductCount()).isEqualTo(wish.getProductCount());
    }

    @Test
    @Description("findById 테스트")
    void findByIdTest() {
        // given
        // when
        Optional<Wish> foundWish = wishRepository.findById(savedWish.getId());

        // then
        assertThat(foundWish).contains(savedWish);
    }

    @Test
    @Description("findAllByMemberId 테스트")
    void findAllByMemberIdTest() {
        // given
        Wish wish1 = new Wish(null, member, product, new ProductCount(5L));
        Wish wish2 = new Wish(null, member, product, new ProductCount(15L));
        wish1 = wishRepository.save(wish1);
        wish2 = wishRepository.save(wish2);
        entityManager.flush();

        // when
        List<Wish> wishes = wishRepository.findAllByMemberId(member.getId());

        // then
        assertThat(wishes).contains(wish1, wish2);
    }



    @Test
    @Description("update 테스트")
    void updateTest() {
        // given
        // when
        savedWish = new Wish(savedWish.getId(), savedWish.getMember(), savedWish.getProduct(), new ProductCount(20L));
        Wish updatedWish = wishRepository.save(savedWish);
        entityManager.flush();

        // then
        assertThat(updatedWish).isEqualTo(savedWish);
    }

    @Test
    @Description("deleteById 테스트")
    void deleteTest() {
        // given
        // when
        wishRepository.deleteById(savedWish.getId());
        List<Wish> wishes = wishRepository.findAllByMemberId(member.getId());

        // then
        assertThat(wishes).isEmpty();
    }
}
