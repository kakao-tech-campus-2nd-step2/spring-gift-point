package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Category;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager em;

    private Member member;
    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("테스트 카테고리", "#6c95d1",
            "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "");
        categoryRepository.save(category);

        member = new Member("test@example.com", "password");
        member = memberRepository.save(member);

        product = new Product("product", 5000, "http://example.com/image1.jpg", category);
        product = productRepository.save(product);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("회원 ID와 상품 ID로 위시리스트 찾기")
    void testFindByMemberIdAndProductId() {
        Wish wish = new Wish(member, product);
        wishRepository.save(wish);

        em.flush();
        em.clear();

        Optional<Wish> wishes = wishRepository.findByMemberIdAndProductId(member.getId(),
            product.getId());
        assertThat(wishes).isPresent();

        Wish wishList = wishes.get();
        assertThat(wishList.getMember().getId()).isEqualTo(member.getId());
        assertThat(wishList.getProduct().getId()).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("회원 ID로 위시리스트 찾기")
    void testFindByMemberId() {
        Product product2 = new Product("product2", 2000, "http://example.com/image2.jpg", category);
        product2 = productRepository.save(product2);

        Wish wish1 = new Wish(member, product);
        Wish wish2 = new Wish(member, product2);
        wishRepository.save(wish1);
        wishRepository.save(wish2);

        List<Wish> wishList = wishRepository.findByMemberId(member.getId());
        assertThat(wishList).hasSize(2);
    }

    @Test
    @DisplayName("위시리스트 저장")
    void testSaveWish() {
        Wish wish = new Wish(member, product);
        Wish saveWish = wishRepository.save(wish);

        assertThat(saveWish).isNotNull();
        assertThat(saveWish.getId()).isNotNull();
        assertThat(saveWish.getMember().getId()).isEqualTo(member.getId());
        assertThat(saveWish.getProduct().getId()).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("회원 ID와 상품 ID로 위시리스트 삭제")
    void testDeleteByMemberIdAndProductId() {
        Wish wish = new Wish(member, product);
        wishRepository.save(wish);

        wishRepository.delete(wish);

        Optional<Wish> wishes = wishRepository.findByMemberIdAndProductId(member.getId(),
            product.getId());
        assertThat(wishes).isEmpty();
    }

    @Test
    @DisplayName("위시리스트 존재 여부 확인")
    void testExistsById() {
        Wish wish = new Wish(member, product);
        Wish saveWish = wishRepository.save(wish);

        boolean exists = wishRepository.existsById(saveWish.getId());
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("위시리스트 ID로 삭제")
    @Transactional
    void testDeleteById() {
        Wish wish = new Wish(member, product);
        Wish saveWish = wishRepository.save(wish);

        wishRepository.deleteById(saveWish.getId());

        em.flush();
        em.clear();

        Optional<Wish> wishList = wishRepository.findById(saveWish.getId());
        assertThat(wishList).isEmpty();
    }

    @Test
    @DisplayName("상품 ID로 위시리스트 삭제")
    void testDeleteByProductId() {
        Wish wish = new Wish(member, product);
        wishRepository.save(wish);

        wishRepository.delete(wish);

        Optional<Wish> wishes = wishRepository.findByMemberIdAndProductId(member.getId(),
            product.getId());
        assertThat(wishes).isEmpty();
    }

    @Test
    @DisplayName("위시리스트 업데이트")
    @Transactional
    void testUpdateWish() {
        Wish wish = new Wish(member, product);
        wishRepository.save(wish);

        Product newProduct = new Product("newProduct", 6000, "http://example.com/image2.jpg",
            category);
        newProduct = productRepository.save(newProduct);

        wish.addWish(member, newProduct);
        Wish updatedWish = wishRepository.save(wish);

        em.flush();
        em.clear();

        Optional<Wish> fetchedWish = wishRepository.findById(updatedWish.getId());
        assertThat(fetchedWish).isPresent();
        assertThat(fetchedWish.get().getProduct().getName()).isEqualTo("newProduct");
        assertThat(fetchedWish.get().getProduct().getPrice()).isEqualTo(6000);
    }

}