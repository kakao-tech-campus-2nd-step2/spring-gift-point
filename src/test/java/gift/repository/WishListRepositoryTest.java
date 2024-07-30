package gift.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import gift.model.Member;
import gift.model.Product;
import gift.model.WishList;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class WishListRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishListRepository wishListRepository;

    @Test
    @DisplayName("위시리스트 저장 및 조회 테스트")
    void saveAndFindWishList() {
        // given
        Member member = new Member("test@example.com", "password123");
        member = memberRepository.save(member);

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(1000);
        product.setImageUrl("http://example.com/image.jpg");
        product = productRepository.save(product);

        WishList wishList = new WishList();
        wishList.setMember(member);
        wishList.setProduct(product);

        // when
        WishList savedWishList = wishListRepository.save(wishList);

        // then
        assertThat(savedWishList.getId()).isNotNull();
        assertThat(savedWishList.getMember().getEmail()).isEqualTo("test@example.com");
        assertThat(savedWishList.getProduct().getName()).isEqualTo("Test Product");
    }
}
