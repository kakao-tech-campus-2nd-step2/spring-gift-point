package gift.RepositoryTest;

import gift.Model.Entity.Category;
import gift.Model.Entity.Member;
import gift.Model.Entity.Product;
import gift.Model.Entity.Wish;
import gift.Repository.CategoryRepository;
import gift.Repository.MemberRepository;
import gift.Repository.ProductRepository;
import gift.Repository.WishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class WishRepositoryTest {
    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager testEntityManager;


    private Member member;
    private Product product1;
    private Product product2;
    private Category category;
    private LocalDateTime createdDate1;
    private LocalDateTime createdDate2;

    @BeforeEach
    void beforeEach(){
        category = categoryRepository.save(new Category("카테고리", "#812f3D", "카테고리 url", "카테고리 description"));
        product1 = productRepository.save(new Product("아메리카노", 4000, "아메리카노url", category));
        product2 = productRepository.save(new Product("카푸치노", 4500, "카푸치노url", category));
        member = memberRepository.save(new Member("woo6388@naver.com", "12345678"));
        createdDate1 = LocalDateTime.now();
        createdDate2 = LocalDateTime.now();
        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    void saveTest() {
        Wish wish = new Wish(member, product1, createdDate1);
        assertThat(wish.getId()).isNull();
        var actual = wishRepository.save(wish);
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void findByMemberTest() {
        Wish wish1 = wishRepository.save(new Wish(member, product1, createdDate1));
        Wish wish2 = wishRepository.save(new Wish(member, product2, createdDate2));
        List<Wish> actual = wishRepository.findWishListByMember(member);
        assertAll(
                ()->assertThat(actual.get(0).getProduct().getName().getValue()).isEqualTo("아메리카노"),
                () -> assertThat(actual.get(0).getCreatedDate()).isEqualTo(createdDate1),
                ()->assertThat(actual.get(1).getProduct().getName().getValue()).isEqualTo("카푸치노"),
                () -> assertThat(actual.get(1).getCreatedDate()).isEqualTo(createdDate2)
        );
    }

    @Test
    void findByMemberAndProductTest() {
        Wish wish1 = wishRepository.save(new Wish(member, product1, createdDate1));
        Wish wish2 = wishRepository.save(new Wish(member, product2, createdDate2));
        Optional<Wish> actual = wishRepository.findByMemberAndProduct(member, product1);
        assertAll(
                ()->assertThat(actual).isPresent(),
                ()->assertThat(actual.get().getCreatedDate()).isEqualTo(createdDate1),
                ()->assertThat(actual.get().getId()).isEqualTo(wish1.getId())
        );
    }

    @Test
    void deleteTest() {
        Wish wish1 = wishRepository.save(new Wish(member, product1, createdDate1));
        Optional<Wish> optionalWish = wishRepository.findById(wish1.getId());
        wishRepository.deleteById(optionalWish.get().getId());
        Optional<Wish> actual = wishRepository.findById(optionalWish.get().getId());
        assertThat(actual).isEmpty();
    }
}
