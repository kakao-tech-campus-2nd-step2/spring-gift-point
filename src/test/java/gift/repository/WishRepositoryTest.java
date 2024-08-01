package gift.repository;

import gift.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") // 테스트 프로파일 사용
public class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    private Wish testWish;
    private Member testMember;
    private Product testProduct;
    private Option testOption;
    private Category testCategory;

    @BeforeEach
    public void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setColor("#FFFFFF");
        testCategory.setImageUrl("http://example.com/image.jpg");

        // 멤버 생성
        testMember = new Member();
        testMember.setEmail("test@example.com");
        testMember.setPassword("password");
        memberRepository.save(testMember);

        // 제품 생성
        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setPrice(1000);
        testProduct.setImageUrl("http://example.com/image.jpg");
        testProduct.setCategory(testCategory);

        // 옵션 생성
        testOption = new Option();
        testOption.setName("Test Option");
        testOption.setQuantity(10);
        testOption.setProduct(testProduct);

        // 제품에 옵션 설정
        testProduct.setOptions(Collections.singletonList(testOption));
        productRepository.save(testProduct);

        // 위시 생성
        testWish = new Wish();
        testWish.setMember(testMember);
        testWish.setProduct(testProduct);
        wishRepository.save(testWish);
    }

    @Test
    public void testFindAllByMemberId_Success() {
        assertThat(wishRepository.findAllByMemberId(testMember.getId(), Pageable.unpaged()).getContent()).hasSize(1);
    }

    @Test
    public void testFindByIdAndMemberId_Success() {
        Optional<Wish> foundWish = wishRepository.findByIdAndMemberId(testWish.getId(), testMember.getId());
        assertThat(foundWish).isPresent();
        assertThat(foundWish.get().getProduct().getName()).isEqualTo(testProduct.getName());
    }

    @Test
    public void testDeleteByMemberAndProduct_Success() {
        wishRepository.deleteByMemberAndProduct(testMember, testProduct);
        Optional<Wish> deletedWish = wishRepository.findById(testWish.getId());
        assertThat(deletedWish).isNotPresent();
    }
}
