package gift.CartItem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import gift.domain.Member.Member;
import gift.domain.category.Category;
import gift.domain.category.JpaCategoryRepository;
import gift.domain.cartItem.CartItem;
import gift.domain.cartItem.JpaCartItemRepository;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import gift.domain.Member.JpaMemberRepository;
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
public class CartItemRepositoryTest {

    @Autowired
    private JpaCartItemRepository cartItemRepository;
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
    void getCartItems() {
        // when
        cartItemRepository.save(new CartItem(member, product1));
        cartItemRepository.save(new CartItem(member, product2));
        List<CartItem> cartItems = cartItemRepository.findAllByMemberId(member.getId());
        // then
        assertThat(cartItems.size()).isEqualTo(2);
    }

    @Test
    @Description("장바구니 추가")
    void addCartItem() {
        // when
        CartItem savedCartItem = cartItemRepository.saveAndFlush(new CartItem(member, product1));
        List<CartItem> cartItems = cartItemRepository.findAllByMember(savedCartItem.getMember());
        // then
        assertThat(cartItems.size()).isEqualTo(1);
        CartItem cartItem = cartItems.get(0);
        assertThat(cartItem.getMember()).isEqualTo(member);
        assertThat(cartItem.getProduct()).isEqualTo(product1);
    }

    @Test
    @Description("장바구니 삭제")
    void deleteCartItem() {
        // when
        CartItem savedCartItem = cartItemRepository.save(new CartItem(member, product1));
        cartItemRepository.deleteById(savedCartItem.getId());
        List<CartItem> cartItems = cartItemRepository.findAllByMemberId(member.getId());
        // then
        assertThat(cartItems.size()).isEqualTo(0);
    }

    @Test
    @Description("지연 로딩 - 쿼리 확인")
    void fetchLazy() {
        // when
        CartItem savedCartItem = cartItemRepository.save(new CartItem(member, product1));
        clear();

        // then
        cartItemRepository.findById(savedCartItem.getId());
    }

    @Test
    @Description("지연 로딩 - 연관 엔티티 조회 시 쿼리 늦게 나감")
    void fetchLazyAndGetLater() {
        // given
        CartItem savedCartItem = cartItemRepository.saveAndFlush(new CartItem(member, product1));
        clear();

        // when
        CartItem findCartItem = cartItemRepository.findById(savedCartItem.getId()).get();
        Member findMember = findCartItem.getMember();
        Product findProduct = findCartItem.getProduct();

        // then - 직접 사용해야 SELECT 쿼리 나감
        System.out.println("findMember = " + findMember);
        System.out.println("findProduct = " + findProduct);
    }

    @Test
    @Description("식별자 vs non 식별자를 사용했을 때 영속성 컨텍스트 내 엔티티 조회 가능 여부 확인")
    void testEntityRetrievalByIdVsByName() {
        // given
        CartItem savedCartItem = cartItemRepository.saveAndFlush(new CartItem(member, product1));

        // when - id(식별자) 조회 -> 영속성 컨텍스트에서 찾을 수 있음, 기타 필드로 조회 -> db 에 쿼리 날려야 함
        cartItemRepository.findById(savedCartItem.getId());
        CartItem cartItem = cartItemRepository.findByMemberIdAndProductId(member.getId(),
            product1.getId()).orElseThrow();
    }

    @Test
    @Description("정상 페이징 확인")
    void testPagingSuccess() {
        // given
        CartItem savedCartItem1 = cartItemRepository.saveAndFlush(new CartItem(member, product1));
        CartItem savedCartItem2 = cartItemRepository.saveAndFlush(new CartItem(member, product2));
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Direction.DESC, "id"));
        clear();

        // when
        Page<CartItem> cartItems = cartItemRepository.findAllByMemberId(member.getId(), pageRequest);

        // then
        assertAll(
            () -> assertThat(cartItems.getTotalElements()).isEqualTo(2), // 전체 CartItem 개수
            () -> assertThat(cartItems.getTotalPages()).isEqualTo(1), // 전체 페이지 개수
            () -> assertThat(cartItems.getNumber()).isEqualTo(pageRequest.getPageNumber()), // 현재 페이지 번호
            () -> assertThat(cartItems.getSize()).isEqualTo(pageRequest.getPageSize()), // 페이지당 보여지는 Product 개수
            () -> assertThat(cartItems.getContent().get(0)).isEqualTo(savedCartItem2),
            () -> assertThat(cartItems.getContent().get(1)).isEqualTo(savedCartItem1)
        );
    }

    private void clear() {
        entityManager.clear();
    }
}
