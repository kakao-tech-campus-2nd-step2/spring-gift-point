package gift.repository;

import gift.common.enums.Role;
import gift.config.JpaConfig;
import gift.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WishRepositoryTest {
    @Autowired
    private WishRepository wishRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("Member id로 Wish Select할 때 fetch join 테스트[성공]")
    void findAllByMemberIdFetchJoin() {
        // given
        String email = "test@gmail.com";
        String password = "password";
        Role role = Role.USER;
        Member member = memberRepository.save(new Member(email, password, role));
        String[] names = {"test1", "test2"};
        int[] prices = {10, 20};
        String[] imageUrls = {"test1", "test2"};
        Category category = categoryRepository.save(new Category("가전", "#123", "url", ""));
        List<Option> options = List.of(new Option("oName", 123));
        Product[] products = {
                productRepository.save(new Product(names[0], prices[0], imageUrls[0], category, options)),
                productRepository.save(new Product(names[1], prices[1], imageUrls[1], category, options))};
        int[] productCounts = {1, 2};
        Wish[] wishes = {
                wishRepository.save(new Wish(member, productCounts[0], products[0])),
                wishRepository.save(new Wish(member, productCounts[1], products[1]))};

        // when
        Page<Wish> actuals = wishRepository.findAllByMemberIdFetchJoin(member.getId(),
                PageRequest.of(0, 10, Sort.by("id").ascending()));

        // then
        assertThat(actuals).hasSize(wishes.length);
        for(int i=0; i < actuals.getContent().size(); i++) {
            Wish result = actuals.getContent().get(i);
            assertThat(result.getId()).isNotNull();
            assertThat(result.getId()).isNotNull();
            assertThat(result.getMember().getEmail()).isEqualTo(email);
            assertThat(result.getMember().getPassword()).isEqualTo(password);
            assertThat(result.getMember().getRole()).isEqualTo(role);

            assertThat(result.getProduct().getCategory().getName()).isEqualTo(category.getName());
            assertThat(result.getProductCount()).isEqualTo(productCounts[i]);
        }
    }

    @Test
    @DisplayName("Wish id로 Select할 때 fetch join 테스트[성공]")
    void findByIdFetchJoin() {
        // given
        String email = "test@gmail.com";
        String password = "password";
        Role role = Role.USER;
        Member member = memberRepository.save(new Member(email, password, role));
        String name = "product1";
        int price = 1000;
        String imageUrl = "imageUrl";
        Category category = categoryRepository.save(new Category("가전", "#123", "url", ""));
        List<Option> options = List.of(new Option("oName", 123));
        Product product = productRepository.save(new Product(name, price, imageUrl, category, options));
        int productCount = 10;
        Wish wish = new Wish(member, productCount, product);

        // when
        Long wishId = wishRepository.save(wish).getId();
        entityManager.clear();  // 영속성 컨텍스트 초기화
        Wish actual = wishRepository.findByIdFetchJoin(wishId).orElseThrow();

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getMember().getEmail()).isEqualTo(email);
        assertThat(actual.getMember().getPassword()).isEqualTo(password);
        assertThat(actual.getMember().getRole()).isEqualTo(role);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getProduct().getName()).isEqualTo(name);
        assertThat(actual.getProduct().getPrice()).isEqualTo(price);
        assertThat(actual.getProduct().getImageUrl()).isEqualTo(imageUrl);
        assertThat(actual.getProduct().getCreatedAt()).isNotNull();
        assertThat(actual.getProduct().getUpdatedAt()).isNotNull();

        assertThat(actual.getProductCount()).isEqualTo(productCount);
    }

    @Test
    @DisplayName("Product와 Member id로 삭제 테스트[성공]")
    void deleteByProductIdAndMemberId() {
        // given
        String email = "test@gmail.com";
        String password = "password";
        Role role = Role.USER;
        Member member = memberRepository.save(new Member(email, password, role));
        String name = "product1";
        int price = 1000;
        String imageUrl = "imageUrl";
        Category category = categoryRepository.save(new Category("가전", "#123", "url", ""));
        List<Option> options = List.of(new Option("oName", 123));
        Product product = productRepository.save(new Product(name, price, imageUrl, category, options));
        int productCount = 10;
        wishRepository.save(new Wish(member, productCount, product));

        // when
        wishRepository.deleteByProductIdAndMemberId(product.getId(), member.getId());
        List<Wish> actuals = wishRepository.findAll();

        // then
        assertThat(actuals.size()).isZero();
    }

    @Test
    @DisplayName("Wish 존재 체크 테스트[성공]")
    void existsByProductIdAndMemberId() {
        // given
        String email = "test@gmail.com";
        String password = "password";
        Role role = Role.USER;
        Member member = memberRepository.save(new Member(email, password, role));
        String name = "product1";
        Category category = categoryRepository.save(new Category("가전", "#123", "url", ""));
        int price = 1000;
        String imageUrl = "imageUrl";
        List<Option> options = List.of(new Option("oName", 123));
        Product product = productRepository.save(new Product(name, price, imageUrl, category, options));
        int productCount = 10;
        wishRepository.save(new Wish(member, productCount, product));

        // when
        boolean actual = wishRepository.existsByProductIdAndMemberId(product.getId(), member.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("Wish 수정 테스트[성공]")
    void update() {
        // given
        Category category = categoryRepository.save(new Category("cname", "color", "imageUrl", "description"));
        Member member = memberRepository.save(new Member("mname", "mage", Role.USER));
        List<Option> options = List.of(new Option("oName", 123));
        Product product = productRepository.save(new Product("pname", 1_000, "pimage", category, options));
        Wish wish = wishRepository.save(new Wish(member, 1, product));
        int productCount = 10;
        wish.updateWish(wish.getMember(), productCount, wish.getProduct());
        entityManager.flush();
        entityManager.clear();

        // when
        Wish actual = wishRepository.findById(wish.getId()).get();
        assertThat(actual).isNotNull();
        assertThat(actual.getProduct().getId()).isEqualTo(product.getId());
        assertThat(actual.getMember().getId()).isEqualTo(member.getId());
        assertThat(actual.getProductCount()).isEqualTo(productCount);
    }
}