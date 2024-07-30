package gift.Wish;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import gift.domain.member.Member;
import gift.domain.category.Category;
import gift.domain.category.JpaCategoryRepository;
import gift.domain.wish.JpaWishRepository;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import gift.domain.member.JpaMemberRepository;
import gift.domain.wish.Wish;
import jakarta.persistence.EntityManager;
import java.util.List;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DataJpaTest
@ActiveProfiles("test")
public class WishRepositoryTest {

    @Autowired
    private JpaWishRepository wishRepository;
    @Autowired
    private JpaMemberRepository memberRepository;
    @Autowired
    private JpaProductRepository productRepository;
    @Autowired
    private JpaCategoryRepository categoryRepository;
    @Autowired
    private EntityManager entityManager;

    private Member member;
    private Product product1;
    private Product product2;
    private Category ethiopia;
    private Category jamaica;

    @BeforeEach
    void setUp() {
        ethiopia = categoryRepository.saveAndFlush(new Category("에티오피아산", "에티오피아 산 원두를 사용했습니다."));
        jamaica = categoryRepository.saveAndFlush(new Category("자메이카산", "자메이카산 원두를 사용했습니다."));
        Member user = new Member("minji@example.com", "password1");
        Member savedMember = memberRepository.saveAndFlush(user);
        this.member = savedMember;

        Product product1 = new Product("아이스 아메리카노 T",  ethiopia, 4500, "https://example.com/image.jpg");
        Product product2 = new Product("아이스 카페모카 M", jamaica, 6300, "https://example.com/image.jpg");
        Product savedProduct1 = productRepository.save(product1);
        Product savedProduct2 = productRepository.save(product2);
        this.product1 = savedProduct1;
        this.product2 = savedProduct2;
    }


    @Test
    @Description("장바구니 조회")
    void getWishes() {
        // when
        wishRepository.save(new Wish(member, product1));
        wishRepository.save(new Wish(member, product2));
        List<Wish> wishes = wishRepository.findAllByMemberId(member.getId());
        // then
        assertThat(wishes.size()).isEqualTo(2);
    }

    @Test
    @Description("장바구니 추가")
    void addWish() {
        // when
        Wish savedWish = wishRepository.saveAndFlush(new Wish(member, product1));
        List<Wish> wishes = wishRepository.findAllByMember(savedWish.getMember());
        clear();
        // then
        assertThat(wishes.size()).isEqualTo(1);
        Wish wish = wishes.get(0);
        assertThat(wish.getMember()).isEqualTo(member);
        assertThat(wish.getProduct()).isEqualTo(product1);
    }

    @Test
    @Description("장바구니 삭제")
    void deleteWish() {
        // when
        Wish savedWish = wishRepository.save(new Wish(member, product1));
        wishRepository.deleteById(savedWish.getId());
        List<Wish> cartItems = wishRepository.findAllByMemberId(member.getId());
        // then
        assertThat(cartItems.size()).isEqualTo(0);
    }

    @Test
    @Description("지연 로딩 - 쿼리 확인")
    void fetchLazy() {
        // when
        Wish savedWish = wishRepository.save(new Wish(member, product1));
        clear();

        // then
        wishRepository.findById(savedWish.getId());
    }

    @Test
    @Description("지연 로딩 - 연관 엔티티 조회 시 쿼리 늦게 나감")
    void fetchLazyAndGetLater() {
        // given
        Wish savedWish = wishRepository.saveAndFlush(new Wish(member, product1));
        clear();

        // when
        Wish findWish = wishRepository.findById(savedWish.getId()).get();
        Member findMember = savedWish.getMember();
        Product findProduct = savedWish.getProduct();

        // then - 직접 사용해야 SELECT 쿼리 나감
        System.out.println("findMember = " + findMember);
        System.out.println("findProduct = " + findProduct);
    }

    @Test
    @Description("식별자 vs non 식별자를 사용했을 때 영속성 컨텍스트 내 엔티티 조회 가능 여부 확인")
    void testEntityRetrievalByIdVsByName() {
        // given
        Wish savedWish = wishRepository.saveAndFlush(new Wish(member, product1));

        // when - id(식별자) 조회 -> 영속성 컨텍스트에서 찾을 수 있음, 기타 필드로 조회 -> db 에 쿼리 날려야 함
        wishRepository.findById(savedWish.getId());
        Wish wish = wishRepository.findByMemberIdAndProductId(member.getId(),
            product1.getId()).orElseThrow();
    }

    @Test
    @Description("정상 페이징 확인")
    void testPagingSuccess() {
        // given
        Wish savedWish1 = wishRepository.saveAndFlush(new Wish(member, product1));
        Wish savedWish2 = wishRepository.saveAndFlush(new Wish(member, product2));
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Direction.DESC, "id"));
        clear();

        // when
        Page<Wish> wishes = wishRepository.findAllByMemberId(member.getId(), pageRequest);

        // then
        assertAll(
            () -> assertThat(wishes.getTotalElements()).isEqualTo(2), // 전체 CartItem 개수
            () -> assertThat(wishes.getTotalPages()).isEqualTo(1), // 전체 페이지 개수
            () -> assertThat(wishes.getNumber()).isEqualTo(pageRequest.getPageNumber()), // 현재 페이지 번호
            () -> assertThat(wishes.getSize()).isEqualTo(pageRequest.getPageSize()), // 페이지당 보여지는 Product 개수
            () -> assertThat(wishes.getContent().get(0)).isEqualTo(savedWish2),
            () -> assertThat(wishes.getContent().get(1)).isEqualTo(savedWish1)
        );
    }

    private void clear() {
        entityManager.clear();
    }
}
