package gift.repository;

import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveAndFindWish() {
        Member member = new Member("test@example.com", "password123");
        memberRepository.save(member);

        Product product = new Product("Test Product", 1000, "http://example.com/image.jpg");
        productRepository.save(product);

        Wish wish = new Wish(member, product);
        Wish savedWish = wishRepository.save(wish);

        List<Wish> wishes = wishRepository.findByMemberId(member.getId());
        assertThat(wishes).isNotEmpty();
        assertThat(wishes.get(0).getProduct().getName()).isEqualTo("Test Product");
    }

    @Test
    public void testDeleteWish() {
        Member member = new Member("test@example.com", "password123");
        memberRepository.save(member);

        Product product = new Product("Test Product", 1000, "http://example.com/image.jpg");
        productRepository.save(product);

        Wish wish = new Wish(member, product);
        wishRepository.save(wish);

        wishRepository.deleteByIdAndMemberId(wish.getId(), member.getId());
        List<Wish> wishes = wishRepository.findByMemberId(member.getId());
        assertThat(wishes).isEmpty();
    }
}
