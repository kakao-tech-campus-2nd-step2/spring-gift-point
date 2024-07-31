package gift.wish;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.member.Member;
import gift.member.MemberRepository;
import gift.product.Product;
import gift.product.ProductRepository;
import gift.wishes.WishPageResponse;
import gift.wishes.WishRepository;
import gift.wishes.WishResponse;
import gift.wishes.WishService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class WishServiceTest {

    @Autowired
    private WishService wishService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WishRepository wishRepository;

    @BeforeEach
    void setup(){
        memberRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        wishRepository.deleteAllInBatch();
        Member member = new Member(1L, "user", "user@gmail.com", "1234");
        Member newMember = memberRepository.save(member);

        for(int i = 0;i<100;i++){
            Product product = new Product("Product" + i, 1234, null, Long.valueOf(i));
            Product newProduct = productRepository.save(product);
            wishService.createWish(newMember.getId(), newProduct.getId(), 1L);
        }
        productRepository.flush();
    }
    @Test
    void pageTest(){
        WishPageResponse wishPage = wishService.getWishPage(1L, 0);
        assertAll(
            () -> assertThat(wishPage.totalPage()).isEqualTo(10),
            () -> assertThat(wishPage.totalElements()).isEqualTo(100),
            () -> assertThat(wishPage.currentPage()).isEqualTo(0)
        );
    }

    @Test
    void listTest(){
        List<WishResponse> wishResponseList = wishService.findByMemberId(2L);
        assertThat(wishResponseList.size()).isEqualTo(100);
    }
}
