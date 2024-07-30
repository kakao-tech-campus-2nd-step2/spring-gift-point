package gift.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import gift.entity.Category;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    private Member member;
    private Product product;

    @BeforeEach
    public void setUp() {
        Category category = new Category("카테고리");
        product = new Product("product", 1000, "imageurl.com",category);
        productRepository.save(product);
        member = new Member("email@email.com", "password");
        memberRepository.save(member);
    }

    @Test
    void save() {
        //given
        Wish expected = new Wish(member, product);
        wishRepository.save(expected);

        //when
        Wish actual = wishRepository.save(expected);

        //then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getMember()).isEqualTo(expected.getMember()),
            () -> assertThat(actual.getProduct()).isEqualTo(expected.getProduct())

        );

    }

    @Test
    void findByMemberId() {
        //given
        Wish expected = new Wish(member, product);
        wishRepository.save(expected);

        //when
        List<Wish> actual = wishRepository.findByMemberId(member.getId());

        //then
        assertAll(
            () -> assertThat(actual.get(0).getId()).isNotNull(),
            () -> assertThat(actual.get(0).getMember()).isEqualTo(member),
            () -> assertThat(actual.get(0).getProduct()).isEqualTo(product)

        );


    }

    @Test
    void deleteWishByMemberIdAndProductId() {
        //given
        Wish expected = new Wish(member, product);
        wishRepository.save(expected);

        //when
        wishRepository.deleteById(expected.getId());
        List<Wish> actual = wishRepository.findByMemberId(expected.getId());

        //then
        assertThat(actual.size()).isEqualTo(0);
    }

}