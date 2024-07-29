package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import gift.product.model.Category;
import gift.product.model.Member;
import gift.product.model.Product;
import gift.product.model.Wish;
import gift.product.repository.AuthRepository;
import gift.product.repository.CategoryRepository;
import gift.product.repository.ProductRepository;
import gift.product.repository.WishRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class WishRepositoryTest {

    @Autowired
    WishRepository wishRepository;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void 위시리스트_항목_추가() {
        //given
        Category category = categoryRepository.save(new Category("테스트카테고리1"));
        Member member = authRepository.save(new Member("test@test.com", "1234"));
        Product product = productRepository.save(new Product("테스트1", 1500, "테스트주소1", category));
        Wish wish = new Wish(member, product);

        //when
        Wish insertedWish = wishRepository.save(wish);

        //then
        assertSoftly(softly -> {
            assertThat(insertedWish.getId()).isNotNull();
            assertThat(insertedWish.getMember().getId()).isNotNull();
            assertThat(insertedWish.getProduct().getId()).isEqualTo(product.getId());
        });
    }

    @Test
    void 위시리스트_전체_조회() {
        //given
        Category category = categoryRepository.save(new Category("테스트카테고리1"));
        Member member = authRepository.save(new Member("test@test.com", "1234"));
        Product product1 = productRepository.save(new Product("테스트1", 1500, "테스트주소1", category));
        Product product2 = productRepository.save(new Product("테스트2", 3000, "테스트주소2", category));

        wishRepository.save(new Wish(member, product1));
        wishRepository.save(new Wish(member, product2));

        //when
        List<Wish> wishes = wishRepository.findAllByMemberId(member.getId());

        //then
        assertThat(wishes).hasSize(2);
    }

    @Test
    void 위시리스트_조회() {
        //given
        Category category = categoryRepository.save(new Category("테스트카테고리1"));
        Member member = authRepository.save(new Member("test@test.com", "1234"));
        Product product = productRepository.save(new Product("테스트1", 1500, "테스트주소1", category));
        Wish wish = wishRepository.save(new Wish(member, product));

        //when
        Optional<Wish> foundWish = wishRepository.findByIdAndMemberId(wish.getId(), member.getId());

        //then
        assertThat(foundWish).isPresent();
        assertThat(foundWish.get().getProduct().getId()).isEqualTo(product.getId());
    }

    @Test
    void 위시리스트_항목_삭제() {
        //given
        Category category = categoryRepository.save(new Category("테스트카테고리1"));
        Member member = authRepository.save(new Member("test@test.com", "1234"));
        Product product = productRepository.save(new Product("테스트1", 1500, "테스트주소1", category));
        Wish wish = wishRepository.save(new Wish(member, product));

        //when
        wishRepository.deleteById(wish.getId());
        boolean isPresentWish = wishRepository.findByIdAndMemberId(wish.getId(), member.getId())
            .isPresent();

        //then
        assertThat(isPresentWish).isFalse();
    }
}
