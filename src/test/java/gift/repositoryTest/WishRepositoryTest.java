package gift.repositoryTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

import gift.domain.category.Category;
import gift.domain.category.CategoryRepository;
import gift.domain.member.Member;
import gift.domain.member.MemberRepository;
import gift.domain.product.Product;
import gift.domain.product.ProductRepository;
import gift.domain.wish.Wish;
import gift.domain.wish.WishRepository;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
@DataJpaTest
public class WishRepositoryTest {
    @Autowired
    private WishRepository wishes;

    @Autowired
    private MemberRepository members;

    @Autowired
    private ProductRepository products;

    @Autowired
    private CategoryRepository categories;

    private Pageable pageable = PageRequest.of(0, 10);

    @Test
    public void exists() {
        Member member = new Member("wjdghtjd06@kakao.com", "1234");
        members.save(member);

        Category category = new Category("교환권", "#6c95d1", "", "https://www.kakao.com");
        categories.save(category);

        Product product = new Product("물건1", 1000L, "image.url", category);
        products.save(product);

        Wish expected = new Wish(member, product, 1L);


        wishes.save(expected);
        System.out.println(product.getId());
        boolean actual = wishes.existsByMemberEmailAndProductId("wjdghtjd06@kakao.com", 1L);

        Assertions.assertThat(actual).isTrue();
    }

    @Test
    public void save() {
        Member member = new Member("wjdghtjd06@kakao.com", "1234");
        members.save(member);

        Category category = new Category("교환권", "#6c95d1", "", "https://www.kakao.com");
        categories.save(category);

        Product product = new Product("물건1", 1000L, "image.url", category);
        products.save(product);

        Wish expected = new Wish(member,product,1L);

        Wish actual = wishes.save(expected);

        Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
        Assertions.assertThat(actual.getProduct()).isEqualTo(expected.getProduct());
        Assertions.assertThat(actual.getCount()).isEqualTo(expected.getCount());
        Assertions.assertThat(actual.getMember()).isEqualTo(expected.getMember());
        Assertions.assertThat(actual.getProduct().getCategory()).isEqualTo(expected.getProduct().getCategory());

    }

    @Test
    public void findAllWithPageable() {
        Category category = new Category("교환권", "#6c95d1", "", "https://www.kakao.com");
        categories.save(category);

        for (int i = 0; i < 10; i++) {
            Member member = new Member("wjdghtjd" + i + "@kakao.com", "1234");
            members.save(member);
            for(int j = 0; j < 10; j++) {
                Product product = new Product("product" + j, 1000L, "image.url", category);
                products.save(product);
                wishes.save(new Wish(member,product,1L));
            }
        }

        var actual = wishes.findAll(pageable);

        Assertions.assertThat(actual).hasSize(10);
        Assertions.assertThat(actual).extracting(Wish::getId)
            .contains(1L,2L,3L,4L,5L,6L,7L,8L,9L,10L);
        Assertions.assertThat(actual).extracting(Wish::getProduct).extracting(Product::getId)
            .contains(1L,2L,3L,4L,5L,6L,7L,8L,9L,10L);
        Assertions.assertThat(actual).extracting(Wish::getMember).extracting(Member::getId)
            .contains(1L);
    }

    @Test
    public void findAllByMemberIdWithPageable() {
        Category category = new Category("교환권", "#6c95d1", "", "https://www.kakao.com");
        categories.save(category);

        for (int i = 0; i < 10; i++) {
            Member member = new Member("wjdghtjd" + i + "@kakao.com", "1234");
            members.save(member);
            for(int j = 0; j < 10; j++) {
                Product product = new Product("product" + j, 1000L, "image.url", category);
                products.save(product);
                wishes.save(new Wish(member,product,1L));
            }
        }

        var actual = wishes.findAllByMemberId(3L, pageable);

        Assertions.assertThat(actual).hasSize(10);
        Assertions.assertThat(actual).extracting(Wish::getMember).extracting(Member::getId)
            .contains(3L);
        Assertions.assertThat(actual).extracting(Wish::getMember).extracting(Member::getId)
            .doesNotContain(2L);
    }

    @Test
    public void updateWish() {
        Category category = new Category("교환권", "#6c95d1", "", "https://www.kakao.com");
        categories.save(category);

        Member member = new Member("wjdghtjd06@kakao.com", "1234");
        members.save(member);

        Product product = new Product("물건1", 1000L, "image.url", category);
        products.save(product);

        Wish expected = new Wish(member,product,1L);
        wishes.save(expected);

        expected.updateWish(3L);

        Assertions.assertThat(expected.getCount()).isEqualTo(3L);
    }

    @Test
    public void deleteWish() {
        Category category = new Category("교환권", "#6c95d1", "", "https://www.kakao.com");
        categories.save(category);

        Member member = new Member("wjdghtjd06@kakao.com", "1234");
        members.save(member);

        Product product = new Product("물건1", 1000L, "image.url", category);
        products.save(product);

        Wish expected = new Wish(member,product,1L);
        wishes.save(expected);

        wishes.delete(expected);
        Assertions.assertThat(wishes.findByMemberIdAndProductId(1L, 1L)).isNotPresent();
    }

    @Test
    void nPlusOneTest() {
        List<Member> memberList = new ArrayList<>();
        Category category = new Category("교환권", "#6c95d1", "", "https://www.kakao.com");
        categories.save(category);

        for (int i = 0; i < 10; i++) {
            memberList.add(new Member("정호성" + i, "1234"));
        }
        members.saveAll(memberList);

        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            productList.add(new Product("상품" + i, 2000L, "image.url", category));
        }
        products.saveAll(productList);

        List<Wish> wishList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Wish wish = new Wish(memberList.get(i), productList.get(i), 2L);
            wishList.add(wish);
        }

        wishes.saveAll(wishList);

        System.out.println("-----------N + 1 문제 테스트------------");
        Page<Wish> everyWishes = wishes.findAll(pageable);
        assertFalse(everyWishes.isEmpty());
    }
}
