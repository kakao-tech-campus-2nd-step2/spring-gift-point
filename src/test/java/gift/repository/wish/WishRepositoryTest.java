package gift.repository.wish;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class WishRepositoryTest {

    @Autowired
    WishRepository wishRepository;

    @Autowired
    EntityManager entityManager;

    private Member member;

    private Product product;

    private Wish wish;

    @BeforeEach
    void setUp(){
        member = new Member.Builder()
                .email("test@pusan.ac.kr")
                .password("abc")
                .build();

        product = new Product.Builder()
                .name("테스트")
                .price(123)
                .imageUrl("abc.png")
                .build();

        wish = new Wish.Builder()
                .member(member)
                .product(product)
                .count(100)
                .build();

        entityManager.persist(member);
        entityManager.persist(product);
        wishRepository.save(wish);
    }

    @Order(1)
    @Test
    @DisplayName("저장 테스트")
    void 저장_테스트(){
        //given
        Member member2 = new Member.Builder()
                .email("test2@pusan.ac.kr")
                .password("abc")
                .build();

        Product product2 = new Product.Builder()
                .name("테스트2")
                .price(123)
                .imageUrl("abc.png")
                .build();

        Wish wish2 = new Wish.Builder()
                .member(member2)
                .product(product2)
                .count(3)
                .build();

        entityManager.persist(member2);
        entityManager.persist(product2);
        entityManager.flush();

        //when
        Wish savedWish = wishRepository.save(wish2);

        //then
        assertAll(
                () -> assertThat(savedWish.getId()).isNotNull(),
                () -> assertThat(savedWish.getMember().getId()).isNotNull(),
                () -> assertThat(savedWish.getMember().getEmail()).isEqualTo(member2.getEmail()),
                () -> assertThat(savedWish.getProduct().getName()).isEqualTo(product2.getName()),
                () -> assertThat(savedWish.getCount()).isEqualTo(wish2.getCount())
        );
    }

    @Test
    @DisplayName("단건 조회")
    void 단건_조회_테스트(){
        //given

        //when
        Wish findWish = wishRepository.findById(wish.getId()).get();

        //then
        assertAll(
                () -> assertThat(findWish.getId()).isNotNull(),
                () -> assertThat(findWish.getMember().getId()).isNotNull(),
                () -> assertThat(findWish.getMember().getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(findWish.getProduct().getName()).isEqualTo(product.getName()),
                () -> assertThat(findWish.getCount()).isEqualTo(wish.getCount())
        );
    }

    @Test
    @DisplayName("위시 이메일로 조회")
    void 위시_이메일_조회_테스트(){
        //given

        //when
        List<Wish> findWishes = wishRepository.findWishByByMemberEmail(member.getEmail());

        //then
        assertThat(findWishes.size()).isEqualTo(1);
        assertThat(findWishes.get(0).getProduct().getName()).isEqualTo("테스트");
    }

    @Test
    @DisplayName("ID 와 EMAIL로 조회")
    void 아이디_이메일_조회(){
        //given
        //when
        Wish findWish = wishRepository.findWishByIdAndMemberEmail(wish.getId(), member.getEmail()).get();
        Optional<Wish> emptyWish = wishRepository.findWishByIdAndMemberEmail(wish.getId(), "없는@pusan.ac.kr");

        //then
        assertAll(
                () -> assertThat(findWish.getId()).isNotNull(),
                () -> assertThat(findWish.getMember().getId()).isNotNull(),
                () -> assertThat(findWish.getMember().getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(findWish.getProduct().getName()).isEqualTo(product.getName()),
                () -> assertThat(findWish.getCount()).isEqualTo(wish.getCount()),
                () -> assertThat(emptyWish.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("page 기능 테스트")
    void 페이지_기능_테스트(){
        //given
        Product product2 = new Product.Builder()
                .name("테스트2")
                .price(2)
                .imageUrl("abc.png")
                .build();

        Wish wish2 = new Wish.Builder()
                .member(member)
                .product(product2)
                .count(2)
                .build();

        entityManager.persist(product2);

        wishRepository.save(wish2);
        entityManager.clear();

        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "count"));

        Page<Wish> wishPage = wishRepository.findWishesByMemberEmail(member.getEmail(), pageRequest);
        List<Wish> wishList = wishPage.stream().toList();

        assertAll(
                () -> assertThat(wishPage.getTotalPages()).isEqualTo(2),
                () -> assertThat(wishPage.getNumberOfElements()).isEqualTo(1),
                () -> assertThat(wishList.get(0).getCount()).isEqualTo(100)
        );
    }




}