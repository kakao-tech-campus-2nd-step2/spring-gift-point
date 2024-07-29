package gift.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.member.persistence.entity.Member;
import gift.member.persistence.entity.Wishlist;
import gift.member.persistence.repository.MemberJpaRepository;
import gift.member.persistence.repository.WishlistJpaRepository;
import gift.product.persistence.entity.Category;
import gift.product.persistence.entity.Product;
import gift.product.persistence.repository.CategoryJpaRepository;
import gift.product.persistence.repository.ProductJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
public class WishlistJpaRepositoryTest {
    @Autowired
    private WishlistJpaRepository wishlistRepository;

    private static Member member;
    private static Product product;

    @BeforeAll
    static void setUp(
        @Autowired MemberJpaRepository memberRepository,
        @Autowired ProductJpaRepository productRepository,
        @Autowired CategoryJpaRepository categoryRepository
    ) {
        Member member = new Member("test@email.com", "test");
        WishlistJpaRepositoryTest.member = memberRepository.save(member);

        var category = categoryRepository.save(new Category("name"));

        Product product = new Product("name", "description", 1000, "http://url.com", category);
        WishlistJpaRepositoryTest.product = productRepository.save(product);
    }

    @AfterEach
    void tearDown() {
        wishlistRepository.deleteAll();
    }

    @Test
    void save() {
        // given
        Wishlist expected = new Wishlist(product, member, 1);

        // when
        Wishlist actual = wishlistRepository.save(expected);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getProduct()).isEqualTo(product)
        );
    }

    @Test
    void findByMemberIdAndProductId() {
        // given
        Wishlist expected = new Wishlist(product, member, 1);
        Wishlist saved = wishlistRepository.save(expected);

        // when
        Wishlist actual = wishlistRepository.findByMemberIdAndProductId(
                member.getId(),
                product.getId())
            .orElse(null);

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getProduct()).isEqualTo(product)
        );
    }

    @Test
    void deleteByMemberIdAndProductId() {
        // given
        Wishlist expected = new Wishlist(product, member, 1);
        Wishlist saved = wishlistRepository.save(expected);

        // when
        wishlistRepository.deleteByMemberIdAndProductId(
            member.getId(),
            product.getId()
        );

        // then
        assertThat(wishlistRepository.findByMemberIdAndProductId(
            member.getId(),
            product.getId()
        )).isEmpty();
    }

    @Test
    void findByMemberId() {
        // given
        Wishlist expected = new Wishlist(product, member, 1);
        Wishlist saved = wishlistRepository.save(expected);


        PageRequest pageRequest = PageRequest.of(0, 1);

        // when
        var actual = wishlistRepository.findByMemberId(member.getId(), pageRequest);

        // then
        assertAll(
            () -> assertThat(actual.hasNext()).isFalse(),
            () -> assertThat(actual.getContent().get(0)).isEqualTo(saved)
        );
    }



}
