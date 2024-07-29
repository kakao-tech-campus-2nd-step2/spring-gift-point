package gift.repository;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackages = "gift.repository")
class MemberRepositoryTest {
    @Autowired
    private  MemberRepository members;
    @Autowired
    private ProductRepository products;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private RepositoryHelper helper;

    @Test
    @DisplayName("멤버 저장 테스트")
    void save() {
        // given
        Member expectedMember = new Member.Builder()
                .email("a@a.com")
                .password("1234")
                .build();

        // when
        Member actual = members.save(expectedMember);

        // then
        assertAll(
                ()->assertThat(actual.getId()).isNotNull(),
                ()->assertThat(actual.getEmail()).isEqualTo(expectedMember.getEmail()),
                ()->assertThat(actual.getPassword()).isEqualTo(expectedMember.getPassword())
        );
    }

    @Test
    @DisplayName("멤버 이메일 조회 테스트")
    void findByEmail() {
        // given
        Member expectedMember = new Member.Builder()
                .email("a@a.com")
                .password("1234")
                .build();
        Member actual = members.save(expectedMember);

        // when
        Member foundMember = members.findByEmail(actual.getEmail()).orElse(null);

        // then
        assertNotNull(foundMember);
        assertAll(
                () -> assertThat(foundMember.getId()).isNotNull(),
                () -> assertThat(foundMember.getEmail()).isEqualTo(expectedMember.getEmail()),
                ()->assertThat(foundMember.getPassword()).isEqualTo(expectedMember.getPassword())
        );
    }

    /*
    @Test
    @DisplayName("멤버 이메일, 비밀번호로 조회 테스트")
    void findByEmailAndPassword() {
        // given
        Member expectedMember = new Member.Builder()
                .email("a@a.com")
                .password("1234")
                .build();
        members.save(expectedMember);

        // when
        Member foundMember = members.findByEmailAndPassword(expectedMember.getEmail(),expectedMember.getPassword()).orElse(null);

        // then
        assertNotNull(foundMember);
        assertAll(
                () -> assertThat(foundMember.getId()).isNotNull(),
                () -> assertThat(foundMember.getEmail()).isEqualTo(expectedMember.getEmail()),
                ()->assertThat(foundMember.getPassword()).isEqualTo(expectedMember.getPassword())
        );
    }

    @Test
    @DisplayName("멤버->위시 영속 전파 테스트")
    void testCascadePersist(){
        // given
        Member expectedMember = new Member.Builder()
                .email("a@a.com")
                .password("1234")
                .build();

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("http://example.com/americano")
                .build();

        Wish expectedWish = new Wish.Builder()
                .member(expectedMember)
                .product(expectedProduct)
                .qunatity(1)
                .build();

        products.save(expectedProduct);
        expectedProduct.addWish(expectedWish);

        // when
        Member savedMember = members.save(expectedMember);
        members.flush();
        entityManager.clear();

        // then
        Member foundMember = helper.findMemberById(savedMember.getId()).orElse(null);
        assertNotNull(foundMember);
        assertAll(
                () -> assertThat(foundMember).isEqualTo(savedMember),
                () -> assertThat(foundMember.getWishes().size()).isEqualTo(1),
                () -> assertThat(foundMember.getWishes().get(0)).isEqualTo(expectedWish)
        );
    }

    @Test
    @DisplayName("멤버->위시 삭제 전파 테스트")
    void testCascadeRemove(){
        // given
        Member expectedMember = new Member.Builder()
                .email("a@a.com")
                .password("1234")
                .build();

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("http://example.com/americano")
                .build();

        Wish expectedWish = new Wish.Builder()
                .member(expectedMember)
                .product(expectedProduct)
                .qunatity(1)
                .build();

        products.save(expectedProduct);
        expectedProduct.addWish(expectedWish);
        Member savedMember = members.save(expectedMember);
        members.flush();
        entityManager.clear();

        // when
        members.deleteById(savedMember.getId());

        //then
        List<Member> foundMembers = members.findAll();
        Wish deletedWish = helper.findWishById(expectedWish.getId()).orElse(null);
        assertAll(
                () -> assertThat(foundMembers.size()).isEqualTo(0),
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

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("http://example.com/americano")
                .build();

        Wish expectedWish = new Wish.Builder()
                .member(expectedMember)
                .product(expectedProduct)
                .qunatity(1)
                .build();

        products.save(expectedProduct);
        expectedProduct.addWish(expectedWish);

        members.save(expectedMember);
        members.flush();
        entityManager.clear();

        Member foundMember = helper.findMemberById(expectedMember.getId()).orElse(null);
        assertNotNull(foundMember);
        Wish foundWish = foundMember.getWishes().get(0);

        // when
        foundMember.removeWish(foundWish);
        members.flush();
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

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("http://example.com/americano")
                .build();

        Wish expectedWish = new Wish.Builder()
                .member(expectedMember)
                .product(expectedProduct)
                .qunatity(1)
                .build();

        products.save(expectedProduct);
        expectedProduct.addWish(expectedWish);
        Member savedMember = members.save(expectedMember);
        members.flush();
        entityManager.clear();

        // when
        // Product 조회 (지연 로딩이므로 연관관계 조회 안함, Product 객체만 조회함)
        Member foundMember = helper.findMemberById(savedMember.getId()).orElse(null);
        assertNotNull(foundMember);
        // Wish 조회 (Wish 객체도 조회함)
        List<Wish> wishes = foundMember.getWishes();

        // then
        assertAll(
                () -> assertThat(wishes.size()).isEqualTo(1),
                () -> assertThat(wishes.get(0)).isEqualTo(expectedWish)
        );
    } */
}