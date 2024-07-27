package gift.wish;

import gift.member.model.Member;
import gift.member.repository.MemberRepository;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import gift.wish.model.WishDTO;
import gift.wish.repository.WishRepository;
import gift.wish.service.WishService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ContextLoadsRelationTest {
    @Autowired
    WishRepository wishRepository;
    @Autowired
    WishService wishService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProductRepository productRepository;

    @Test
    @Transactional
    public void contextLoads() {
        // given
        Member member1 = new Member("animoto1@naver.com", "1234");
        memberRepository.save(member1);

        Product product1 = new Product("Ice Americano", 2000, "http://example.com/example.jpg");
        productRepository.save(product1);
        Product product2 = new Product("Hot Americano", 2000, "http://example.com/example.jpg");
        productRepository.save(product2);

        // when
        wishService.createWish(member1, product1.getId());
        wishService.createWish(member1, product2.getId());

        // then : 위시리스트에 추가된 상품 정보 출력
        Member member = memberRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        List<WishDTO> wishlist = wishService.getWishlistByMemberId(member);
        assertThat(wishlist).hasSize(2);
        assertThat(wishlist.get(0).getProductName()).isEqualTo("Ice Americano");

        // WishDTO를 통해 Product 정보를 출력
        for (WishDTO wishDTO : wishlist) {
            System.out.println("Product ID: " + wishDTO.getProductId());
            System.out.println("Product Name: " + wishDTO.getProductName());
            System.out.println("Product Price: " + wishDTO.getProductPrice());
            System.out.println("Product Image URL: " + wishDTO.getProductImageUrl());
        }
    }

}
