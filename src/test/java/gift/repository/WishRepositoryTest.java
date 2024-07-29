package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.model.Category;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class WishRepositoryTest {

    @Autowired
    WishRepository wishRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Wish wish;
    Member member;
    Product product;
    Category category;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("aaa123@a.com", "1234"));
        category = categoryRepository.save(new Category("카테고리"));
        product = productRepository.save(new Product("productA", 1000, "https://a.com", category));
        wish = new Wish(member, product);
    }

    @Test
    @DisplayName("WishProduct insert 테스트")
    void insert() {
        assertThat(wish.getId()).isNull();
        Wish saved = wishRepository.save(wish);
        assertThat(saved.getId()).isNotNull();

        // 같은 위시상품을 중복해서 담을 수 없다.
        Wish duplicated = wishRepository.save(wish);
        assertThat(saved).isEqualTo(duplicated);
    }

    @Test
    @DisplayName("WishProduct delete 테스트 ")
    void delete() {
        Wish saved = wishRepository.save(wish);
        wishRepository.delete(saved);

        // 삭제된 위시상품을 조회할 수 없어야 한다.
        assertThat(wishRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("WishProduct findAll 테스트")
    void findAll() {
        List<Product> products = new ArrayList<>();
        IntStream.range(0, 10)
            .forEach( i -> {
                products.add(new Product("product"+i, 1000, "https://a.com", category));
            });

        products.forEach(
            product -> {
                Product saved = productRepository.save(product);
                Wish wish = new Wish(member, product);
                wishRepository.save(wish);
            }
        );

        List<Wish> findWishes = wishRepository.findAll();
        assertThat(findWishes).hasSize(10);

        IntStream.range(0, 10)
                .forEach(i -> {
                    Wish w = findWishes.get(i);
                    assertThat(w.getId()).isNotNull();
                    assertThat(w.getProduct().getId()).isEqualTo(products.get(i).getId());
                });

    }


}