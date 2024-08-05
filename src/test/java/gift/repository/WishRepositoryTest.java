package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.domain.Category;
import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.repository.fixture.CategoryFixture;
import gift.repository.fixture.MemberFixture;
import gift.repository.fixture.ProductFixture;
import gift.repository.fixture.WishFixture;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@DataJpaTest
class WishRepositoryTest {
    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("Create Test")
    void save(){
        // given
        Category category = CategoryFixture.createCategory("categoryName","color","description","url");
        categoryRepository.save(category);
        Member member = MemberFixture.createMember("user@example.com", "password");
        memberRepository.save(member);
        Product product = ProductFixture.createProduct("test",100,"kkk",category);
        productRepository.save(product);
        Wish expected = WishFixture.createWish(member,product,5);
        // when
        Wish actual = wishRepository.save(expected);
        em.flush();
        em.clear();
        // then
        assertAll(
            ()->assertThat(actual.getId()).isNotNull(),
            ()->assertThat(actual.getId()).isEqualTo(expected.getId())
        );
    }

    @Test
    @DisplayName("Find Wishes By Member ID Test")
    void findByMemberId() {
        // given
        Category category = CategoryFixture.createCategory("categoryName","color","description","url");
        categoryRepository.save(category);
        Member member = MemberFixture.createMember("user@example.com", "password");
        memberRepository.save(member);
        Product product = ProductFixture.createProduct("test",100,"kkk",category);
        productRepository.save(product);

        Wish expected = WishFixture.createWish(member,product,5);
        wishRepository.save(expected);
        em.flush();
        em.clear();

        // when
        List<Wish> actuals = wishRepository.findByMemberId(member.getId()).get();

        // then
        assertAll(
            () -> assertThat(actuals.size()).isEqualTo(1),
            () -> assertThat(actuals.get(0).getProduct().getName()).isEqualTo(expected.getProduct().getName())
        );
    }

    @Test
    @DisplayName("Find Wish By Member ID And Product Name Test")
    void findByMemberIdAndProductId() {
        // given
        Category category = CategoryFixture.createCategory("categoryName","color","description","url");
        categoryRepository.save(category);
        Member member = MemberFixture.createMember("user@example.com", "password");
        memberRepository.save(member);
        Product product = ProductFixture.createProduct("test",100,"kkk",category);
        productRepository.save(product);

        Wish expected = WishFixture.createWish(member,product,5);
        wishRepository.save(expected);
        em.flush();
        em.clear();

        // when
        Wish actual = wishRepository.findByMemberIdAndProductId(member.getId(),product.getId()).get();

        // then
        assertAll(
            () -> assertThat(actual.getMember().getId()).isEqualTo(expected.getMember().getId()),
            () -> assertThat(actual.getProduct().getName()).isEqualTo(expected.getProduct().getName())
        );
    }

    @Test
    void findAllByMemberInOrderByCreatedAt(){
        // given
        Category category = CategoryFixture.createCategory("categoryName","color","description","url");
        categoryRepository.save(category);
        Member member = MemberFixture.createMember("user@example.com", "password");
        memberRepository.save(member);
        Product product1 = ProductFixture.createProduct("test1",100,"@.@",category);
        productRepository.save(product1);
        Product product2 = ProductFixture.createProduct("test2",100,"@.@",category);
        productRepository.save(product2);

        Wish expected1 = WishFixture.createWish(member,product1,5);
        wishRepository.save(expected1);
        Wish expected2 = WishFixture.createWish(member,product2,5);
        wishRepository.save(expected2);
        em.flush();
        em.clear();
        Pageable pageable = PageRequest.of(0,10, Sort.by("createdAt").ascending());

        // when
        Page<Wish> wishesPage = wishRepository.findAllByMemberIdOrderByCreatedAt(member.getId(),pageable);

        // then
        assertThat(wishesPage).isNotNull();
        assertThat(wishesPage.getContent()).hasSize(2);

        List<Wish> wishes = wishesPage.getContent();
        assertThat(wishes.get(0).getProduct().getName()).isEqualTo(product1.getName());
        assertThat(wishes.get(1).getProduct().getName()).isEqualTo(product2.getName());
    }

    @Test
    void delteById(){
        // given
        Category category = CategoryFixture.createCategory("categoryName","color","description","url");
        categoryRepository.save(category);
        Member member = MemberFixture.createMember("user@example.com", "password");
        memberRepository.save(member);
        Product product = ProductFixture.createProduct("test",100,"kkk",category);
        productRepository.save(product);

        Wish expected = WishFixture.createWish(member,product,5);
        wishRepository.save(expected);
        em.flush();
        em.clear();
        // when
        wishRepository.deleteById(member.getId());
        Optional<Wish> actual = wishRepository.findById(member.getId());
        // then
       assertThat(actual).isEmpty();
    }
}