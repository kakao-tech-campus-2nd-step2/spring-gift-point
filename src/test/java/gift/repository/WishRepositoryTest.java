package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.model.Category;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;


@DataJpaTest
@Transactional
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

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
        // 카테고리 생성
        category = categoryRepository.save(new Category("테스트 카테고리"));

        // 멤버 생성
        member = memberRepository.save(new Member("test@email.com", "test1234"));

        // 상품 생성
        product = new Product("productName", 10000, "image.jpg");
        product.setCategory(category);
        product = productRepository.save(product);
        // 기존 위시 데이터 삭제
        wishRepository.deleteAll();
    }

    @Test
    void saveWishTest() {
        // given
        Wish wish = new Wish(product, member);

        // when
        Wish savedWish = wishRepository.save(wish);

        // then
        assertThat(savedWish).isNotNull();
//        assertThat(savedWish.getProduct()).isEqualTo(product);
//        assertThat(savedWish.getMember()).isEqualTo(member);
    }

    @Test
    void findWishByProductIdTest() {
        // given
        Wish wish = wishRepository.save(new Wish(product, member));

        // when
        var wishList = wishRepository.findByProductId(product.getId());

        // then
        assertThat(wishList).hasSize(1);
        assertThat(wishList.getFirst()).isEqualTo(wish);
    }

    @Test
    void findWishByMemberTest() {
        // given
        Wish wish = wishRepository.save(new Wish(product, member));

        // when
        var wishList = wishRepository.findByMember(member);

        // then
        assertThat(wishList).hasSize(1);
        assertThat(wishList.getFirst()).isEqualTo(wish);
    }

    @Test
    void deleteWishByMemberIdAndIdTest() {
        // given
        Wish wish = wishRepository.save(new Wish(product, member));

        // when
        int deletedCount = wishRepository.deleteByIdAndMember(wish.getId(), member);

        // then
        assertThat(deletedCount).isEqualTo(1);
        assertThat(wishRepository.findById(wish.getId())).isEmpty();
    }
}
//class WishRepositoryTest {
//
//    @Autowired
//    private WishRepository wishRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    private Member member;
//    private Product product;
//
//    @BeforeEach
//    public void setUp() {
//        member = memberRepository.save(new Member("test@email.com", "test1234"));
//        var newProduct = new Product("productName", 10000, "image.jpg");
//        newProduct.setId(20L);
//        product = productRepository.save(newProduct);
//    }
//
//    @Test
//    void saveWishTest() {
//        // given
////        var product1 = new Product("product", 100, "image.jp");
////        var member1 = new Member("email@mail.com", "apssword");
//        Wish wish = new Wish(product, member);
//
//        // when
//        Wish savedWish = wishRepository.save(wish);
//
//        // then
//        assertThat(savedWish).isNotNull();
//        assertThat(savedWish).isEqualTo(wish);
//    }
//
//    @Test
//    void findWishByProductIdTest() {
//        // given
//        Wish wish = wishRepository.save(new Wish(product, member));
//
//        // when
//        var wishList = wishRepository.findByProductId(product.getId());
//
//        // then
//        assertThat(wishList).hasSize(1);
//        assertThat(wishList.getFirst()).isEqualTo(wish);
//    }
//
//    @Test
//    void findWishByMemberTest() {
//        // given
//        Wish wish = wishRepository.save(new Wish(product, member));
//
//        // when
//        var wishList = wishRepository.findByMember(member);
//
//        // then
//        assertThat(wishList).hasSize(1);
//        assertThat(wishList.getFirst()).isEqualTo(wish);
//    }
//
//    @Test
//    void deleteWishByMemberIdAndIdTest() {
//        // given
//        Wish wish = wishRepository.save(new Wish(product, member));
//
//        // when
//        int deletedCount = wishRepository.deleteByIdAndMember(wish.getId(), member);
//
//        // then
//        assertThat(deletedCount).isEqualTo(1);
//        assertThat(wishRepository.findById(wish.getId())).isEmpty();
//    }
//}