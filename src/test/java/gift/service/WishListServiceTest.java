package gift.service;

import static org.assertj.core.api.Assertions.assertThat;

import gift.repository.JpaMemberRepository;
import gift.repository.JpaProductRepository;
import gift.repository.JpaWishRepository;
import gift.member.entity.Member;
import gift.member.MemberRole;
import gift.product.entity.Product;
import gift.wish.entity.Wish;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
class WishListServiceTest {
    @Autowired
    private JpaWishRepository jpaWishRepository;
    @Autowired
    private JpaMemberRepository jpaMemberRepository;
    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Test
    @DisplayName("새로운 상품 목록 추가")
    void addProduct(){
        //given
        Member member = new Member(null,"hah@ha","1234", MemberRole.COMMON_MEMBER);
        Product product = new Product(null,"tuna",4000, "test");

        member = jpaMemberRepository.save(member);
        product = jpaProductRepository.save(product);

        //when
        Wish wish = new Wish(member,product);
        wish = jpaWishRepository.save(wish);


        //then
        assertThat(jpaWishRepository.findAllByMemberId(member.getId())).contains(wish);
        assertThat(wish.getProduct()).isEqualTo(product);

    }

    @Test
    @DisplayName("새로운 상품 여러개 추가")
    void addMultiProduct(){
        //given
        Member member = new Member(null,"hah@ha","1234", MemberRole.COMMON_MEMBER);
        Product product = new Product(null,"tuna",4000, "test");
        Product product2 = new Product(null,"tuna2",5000, "test2");
        Product product3 = new Product(null,"tuna3",6000, "test3");

        member = jpaMemberRepository.save(member);
        product = jpaProductRepository.save(product);
        product2 = jpaProductRepository.save(product2);
        product3 = jpaProductRepository.save(product3);

        //when
        Wish wish1 = new Wish(member,product);
        Wish wish2 = new Wish(member,product2);
        Wish wish3 = new Wish(member,product3);
        wish1 = jpaWishRepository.save(wish1);
        wish2 = jpaWishRepository.save(wish2);
        wish3 = jpaWishRepository.save(wish3);

        //then

        assertThat(jpaWishRepository.findAllByMemberId(member.getId())).hasSize(3);




    }

}