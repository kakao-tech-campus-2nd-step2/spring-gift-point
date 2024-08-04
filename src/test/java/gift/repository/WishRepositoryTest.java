package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Category;
import gift.entity.Product;
import gift.entity.Member;
import gift.entity.Wish;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class WishRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Member member;
    private Member member2;

    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        productRepository.deleteAll();
        memberRepository.deleteAll();
        wishRepository.deleteAll();
        categoryRepository.deleteAll();

        categoryRepository.save(new Category("테스트1", "#000000", "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg", ""));

        member = memberRepository.save(new Member("12345@12345.com", "1", "basic", "홍길동", "user"));
        member2 = memberRepository.save(new Member("22345@12345.com", "2", "basic", "라이언", "user"));
        product = productRepository.save(new Product("커피", 10000,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
                categoryRepository.findByName("테스트1").get()));
    }

    @AfterEach
    void tearDown() throws Exception {
        wishRepository.deleteAll();
        productRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("위시리스트 추가")
    void save(){
        Wish wish = new Wish(member, product, LocalDateTime.now(),1);
        Wish actualWish = wishRepository.save(wish);

        assertThat(actualWish.getProduct()).isEqualTo(product);
        assertThat(actualWish.getMember()).isEqualTo(member);
        assertThat(actualWish.getQuantity()).isEqualTo(1);

        wish.incrementQuantity();
        wishRepository.flush();

        assertThat(wishRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("위시리스트 개수 수정")
    void updateQuantity(){
        Wish wish = new Wish(member, product,  LocalDateTime.now(), 1);
        wishRepository.save(wish);
        assertThat(wishRepository.findByMemberAndProduct(member, product).get().getQuantity()).isEqualTo(1);
        wish.changeQuantity(3);
        wishRepository.flush();

        assertThat(wishRepository.findByMemberAndProduct(member, product).get().getQuantity()).isEqualTo(3);
    }

    @Test
    @DisplayName("위시리스트 Member & 상품 ID로 제거")
    void delete1(){
        Wish wish = new Wish(member, product,  LocalDateTime.now(), 1);
        wishRepository.save(wish);
        assertThat(wishRepository.count()).isEqualTo(1);

        wishRepository.deleteByMemberAndProductId(member, product.getId());
        assertThat(wishRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("위시리스트 상품 ID로 제거")
    void delete2(){
        Wish wish = new Wish(member, product,  LocalDateTime.now(), 1);
        wishRepository.save(wish);
        Wish wish2 = new Wish(member2, product,  LocalDateTime.now(), 1);
        wishRepository.save(wish2);


        assertThat(wishRepository.count()).isEqualTo(2);
        wishRepository.deleteAllByProductId(product.getId());
        assertThat(wishRepository.count()).isEqualTo(0);
    }


}