package gift.repository;

import gift.domain.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackages = "gift.repository")
class ProductRepositoryTest {
    @Autowired
    private ProductRepository products;
    @Autowired
    private  MemberRepository members;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private RepositoryHelper helper;
    @Autowired
    private CategoryRepository categories;

    @Test
    @DisplayName("상품 저장 테스트")
    void save() {
        // given
        Category category = new Category.Builder()
                .name("교환권")
                .color("#FF5733")
                .imageUrl("https://example.com/images/exchange_voucher.jpg")
                .description("다양한 상품으로 교환 가능한 교환권")
                .build();

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("https://example.com/americano")
                .category(category)
                .build();
        categories.save(category);

        // when
        Product actual = products.save(expectedProduct);

        // then
        assertAll(
                ()->assertThat(actual.getId()).isNotNull(),
                ()->assertThat(actual.getName()).isEqualTo(expectedProduct.getName()),
                ()->assertThat(actual.getPrice()).isEqualTo(expectedProduct.getPrice()),
                ()->assertThat(actual.getImageUrl()).isEqualTo(expectedProduct.getImageUrl())
        );
    }

    @Test
    @DisplayName("상품 아이디 조회 테스트")
    void findById() {
        // given
        Category category = new Category.Builder()
                .name("교환권")
                .color("#FF5733")
                .imageUrl("https://example.com/images/exchange_voucher.jpg")
                .description("다양한 상품으로 교환 가능한 교환권")
                .build();

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("https://example.com/americano")
                .category(category)
                .build();
        categories.save(category);
        Product savedProduct = products.save(expectedProduct);

        // when
        Product foundProduct = products.findById(savedProduct.getId()).orElse(null);

        // then
        assertNotNull(foundProduct);
        assertAll(
                () -> assertThat(foundProduct.getId()).isNotNull(),
                () -> assertThat(foundProduct).isEqualTo(expectedProduct)
        );
    }

    @Test
    @DisplayName("상품 이름 조회 테스트")
    void findByName() {
        // given
        Category category = new Category.Builder()
                .name("교환권")
                .color("#FF5733")
                .imageUrl("https://example.com/images/exchange_voucher.jpg")
                .description("다양한 상품으로 교환 가능한 교환권")
                .build();

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("https://example.com/americano")
                .category(category)
                .build();
        categories.save(category);
        Product savedProduct = products.save(expectedProduct);

        // when
        Product foundProduct = products.findByName(savedProduct.getName()).orElse(null);

        // then
        assertNotNull(foundProduct);
        assertAll(
                () -> assertThat(foundProduct.getId()).isNotNull(),
                () -> assertThat(foundProduct).isEqualTo(expectedProduct)
        );
    }

    @Test
    @DisplayName("상품 전체 조회 테스트")
    void findAll() {
        // given
        Category category = new Category.Builder()
                .name("교환권")
                .color("#FF5733")
                .imageUrl("https://example.com/images/exchange_voucher.jpg")
                .description("다양한 상품으로 교환 가능한 교환권")
                .build();
        categories.save(category);

        Option option = new Option("옵션1", 10);
        products.save(new Product("상품1", 1000, "http://product1", category, option));
        products.save(new Product("상품2", 2000, "http://product2", category, option));
        products.save(new Product("상품3", 3000, "http://product3", category, option));

        // when
        List<Product> findProducts = products.findAll();

        // then
        assertThat(findProducts.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("상품 아이디로 삭제 테스트")
    void deleteById() {
        // given
        Category category = new Category.Builder()
                .name("교환권")
                .color("#FF5733")
                .imageUrl("https://example.com/images/exchange_voucher.jpg")
                .description("다양한 상품으로 교환 가능한 교환권")
                .build();

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("https://example.com/americano")
                .category(category)
                .build();
        categories.save(category);
        Product savedProduct = products.save(expectedProduct);

        // when
        savedProduct.remove();
        products.deleteById(savedProduct.getId());

        // then
        List<Product> foundProducts = products.findAll();
        assertAll(
                () -> assertThat(foundProducts.size()).isEqualTo(0)
        );
    }

    /*
    @Test
    @DisplayName("상품->위시 영속 전파 테스트")
    void testCascadePersist(){
        // given
        Member expectedMember = new Member.Builder()
                .email("a@a.com")
                .password("1234")
                .build();

        Category category = new Category.Builder()
                .name("교환권")
                .color("#FF5733")
                .imageUrl("https://example.com/images/exchange_voucher.jpg")
                .description("다양한 상품으로 교환 가능한 교환권")
                .build();

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("https://example.com/americano")
                .category(category)
                .build();

        Wish expectedWish = new Wish.Builder()
                .member(expectedMember)
                .product(expectedProduct)
                .qunatity(1)
                .build();

        categories.save(category);
        members.save(expectedMember);
        expectedProduct.addWish(expectedWish);

        // when
        Product savedProduct = products.save(expectedProduct);
        products.flush();
        entityManager.clear();

        // then
        Product foundProduct = helper.findProductById(savedProduct.getId()).orElse(null);
        assertNotNull(foundProduct);
        assertAll(
                () -> assertThat(foundProduct).isEqualTo(savedProduct),
                () -> assertThat(foundProduct.getWishes().size()).isEqualTo(1),
                () -> assertThat(foundProduct.getWishes().get(0)).isEqualTo(expectedWish)
        );
    }

    @Test
    @DisplayName("상품->위시 삭제 전파 테스트")
    void testCascadeRemove(){
        // given
        Member expectedMember = new Member.Builder()
                .email("a@a.com")
                .password("1234")
                .build();

        Category category = new Category.Builder()
                .name("교환권")
                .color("#FF5733")
                .imageUrl("https://example.com/images/exchange_voucher.jpg")
                .description("다양한 상품으로 교환 가능한 교환권")
                .build();

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("https://example.com/americano")
                .category(category)
                .build();

        Wish expectedWish = new Wish.Builder()
                .member(expectedMember)
                .product(expectedProduct)
                .qunatity(1)
                .build();

        categories.save(category);
        members.save(expectedMember);
        expectedProduct.addWish(expectedWish);
        Product savedProduct = products.save(expectedProduct);
        products.flush();
        entityManager.clear();

        // when
        products.deleteById(savedProduct.getId());

        //then
        List<Product> findProducts = products.findAll();
        Wish deletedWish = entityManager.find(Wish.class, expectedWish.getId());
        assertAll(
                () -> assertThat(findProducts.size()).isEqualTo(0),
                () -> assertThat(deletedWish).isNull()
        );
    }

    @Test
    @DisplayName("고아 객체 제거 테스트")
    void testOrphanRemoval(){
        // given
        Member expectedMember = new Member.Builder()
                .email("a@a.com")
                .password("1234")
                .build();

        Category category = new Category.Builder()
                .name("교환권")
                .color("#FF5733")
                .imageUrl("https://example.com/images/exchange_voucher.jpg")
                .description("다양한 상품으로 교환 가능한 교환권")
                .build();

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("https://example.com/americano")
                .category(category)
                .build();

        Wish expectedWish = new Wish.Builder()
                .member(expectedMember)
                .product(expectedProduct)
                .qunatity(1)
                .build();

        categories.save(category);
        members.save(expectedMember);
        expectedProduct.addWish(expectedWish);

        products.save(expectedProduct);
        products.flush();
        entityManager.clear();

        Product foundProduct = helper.findProductById(expectedProduct.getId()).orElse(null);
        assertNotNull(foundProduct);
        Wish foundWish = foundProduct.getWishes().get(0);

        // when
        foundProduct.removeWish(foundWish);
        products.flush();
        entityManager.clear();

        // then
        Wish orphanedWish = helper.findWishById(expectedWish.getId()).orElse(null);
        assertThat(orphanedWish).isNull();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    void testLazyFetch(){
        // given
        Member expectedMember = new Member.Builder()
                .email("a@a.com")
                .password("1234")
                .build();

        Category category = new Category.Builder()
                .name("교환권")
                .color("#FF5733")
                .imageUrl("https://example.com/images/exchange_voucher.jpg")
                .description("다양한 상품으로 교환 가능한 교환권")
                .build();

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("https://example.com/americano")
                .category(category)
                .build();

        Wish expected = new Wish.Builder()
                .member(expectedMember)
                .product(expectedProduct)
                .qunatity(1)
                .build();

        Wish expected2 = new Wish.Builder()
                .member(expectedMember)
                .product(expectedProduct)
                .qunatity(2)
                .build();

        Wish expected3 = new Wish.Builder()
                .member(expectedMember)
                .product(expectedProduct)
                .qunatity(3)
                .build();

        expectedProduct.addWish(expected);
        expectedProduct.addWish(expected2);
        expectedProduct.addWish(expected3);

        categories.save(category);
        members.save(expectedMember);
        products.save(expectedProduct);
        products.flush();
        entityManager.clear();

        // when
        // Product 조회 (지연 로딩이므로 연관관계 조회 안함, Product 객체만 조회함)
        Product foundProduct = helper.findProductById(expectedProduct.getId()).orElse(null);
        assertNotNull(foundProduct);

        // Wish 조회 (Wish 객체도 조회함)
        List<Wish> wishes = foundProduct.getWishes();

        // then
        assertAll(
                () -> assertThat(wishes.size()).isEqualTo(3),
                () -> assertThat(wishes.get(0)).isEqualTo(expected)
        );
    }

    @Test
    @DisplayName("상품 페이지 조회 테스트")
    void testFindAll() {
        // given
        Category category = new Category.Builder()
                .name("교환권")
                .color("#FF5733")
                .imageUrl("https://example.com/images/exchange_voucher.jpg")
                .description("다양한 상품으로 교환 가능한 교환권")
                .build();

        categories.save(category);
        Option option = new Option("옵션1", 10);
        products.save(new Product("상품1", 1000, "http://product1", category, option));
        products.save(new Product("상품2", 2000, "http://product2", category, option));
        products.save(new Product("상품3", 3000, "http://product3", category, option));

        // when
        Pageable pageable = PageRequest.of(0,2, Sort.by("id").descending());
        Page<Product> page = products.findAll(pageable);

        // then
        assertAll(
                () -> assertThat(page.getContent().size()).isEqualTo(2),
                () -> assertThat(page.getContent().get(0).getName()).isEqualTo("상품3"),
                () -> assertThat(page.getTotalElements()).isEqualTo(3),
                () -> assertThat(page.getTotalPages()).isEqualTo(2)
        );
    } */
}