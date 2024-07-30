package gift.repositories;//package gift.repositories;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import gift.domain.Category;
//import gift.domain.Member;
//import gift.domain.Option;
//import gift.domain.Product;
//import gift.domain.Role;
//import gift.domain.Wish;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//@DataJpaTest
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
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    private Pageable pageable = PageRequest.of(0, 10);
//
//    @Test
//    @DisplayName("특정 회원의 Wishlist 조회 테스트")
//    void testFindWishListById() {
//        // given
//        Member member = new Member(null, "test@example.com", "password", Role.USER);
//        memberRepository.save(member);
//
//        Category category = new Category("Test Category", "Blue", "abcd", "Test");
//        categoryRepository.save(category);
//
//        Product product1 = new Product(null, "testProdut1", 300, "imageUrl", category);
//        productRepository.save(product1);
//
//        Product product2 = new Product(null, "testProdut2", 300, "imageUrl", category);
//        productRepository.save(product2);
//
//        Wish wish1 = new Wish(member, product1);
//
//        Wish wish2 = new Wish(member, product2);
//
//        wishRepository.save(wish1);
//        wishRepository.save(wish2);
//
//        // when
//        Page<Wish> wishList = wishRepository.findAllByMemberId(member.getId(), pageable);
//
//        // then
//        assertThat(wishList).hasSize(2);
//        assertThat(wishList).extracting(Wish::getProduct).extracting(Product::getId)
//            .containsExactlyInAnyOrder(product1.getId(), product2.getId());
//    }
//
//    @Test
//    @DisplayName("Wish 추가 테스트")
//    void testAddWish() {
//        // given
//        Member member = new Member(null, "test2@example.com", "password", Role.USER);
//        memberRepository.save(member);
//
//        Category category = new Category("Test Category", "Blue", "abcd", "Test");
//        categoryRepository.save(category);
//
//        Product product = new Product(null, "testProdut3", 300, "imageUrl", category);
//        productRepository.save(product);
//
//        Wish wish = new Wish(member, product);
//
//        // when
//        wishRepository.save(wish);
//
//        // then
//        List<Wish> wishList = wishRepository.findAllByMemberId(member.getId(), Pageable.unpaged())
//            .getContent();
//        assertThat(wishList).hasSize(1);
//        assertThat(wishList.get(0).getProduct().getId()).isEqualTo(product.getId());
//    }
//
//    @Test
//    @DisplayName("Wish 삭제 테스트")
//    void testDeleteWish() {
//        // given
//        Member member = new Member(null, "test3@example.com", "password", Role.USER);
//        memberRepository.save(member);
//
//        Category category = new Category("Test Category", "Blue", "abcd", "Test");
//        categoryRepository.save(category);
//
//        Product product = new Product(null, "testProdut4", 300, "imageUrl", category);
//        productRepository.save(product);
//
//        Wish wish = new Wish(member, product);
//        wishRepository.save(wish);
//
//        // when
//        wishRepository.deleteByMemberIdAndProductId(member.getId(), product.getId());
//
//        // then
//        List<Wish> wishList = wishRepository.findAllByMemberId(member.getId(), Pageable.unpaged())
//            .getContent();
//        assertThat(wishList).isEmpty();
//    }
//}
