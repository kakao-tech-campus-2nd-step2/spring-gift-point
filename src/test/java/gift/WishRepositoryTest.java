package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.domain.Category;
import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.repository.CategoryRepository;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DataJpaTest
public class WishRepositoryTest {
    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("위시 추가 성공")
    void addWish() {
        Member member = new Member("testemail@email", "testpassword");
        Member savedMember = memberRepository.save(member);
        Category category = new Category("category", "color", "image", "");
        categoryRepository.save(category);
        Product product = new Product("Test", 1000, "test.jpg", category);
        Product savedProduct = productRepository.save(product);
        Wish wish = new Wish(savedMember, savedProduct);

        Wish savedWish = wishRepository.save(wish);
        Member wishMember = savedWish.getMember();

        List<Wish> addedWishes = wishRepository.findByMember(wishMember);
        Wish addedWish = addedWishes.get(0);

        assertThat(addedWish.getMember()).isEqualTo(member);
        assertThat(addedWish.getProduct()).isEqualTo(product);
    }

    @Test
    @DisplayName("위시 삭제 성공")
    void deleteWish() {
        Member member = new Member("testemail@email", "testpassword");
        Member savedMember = memberRepository.save(member);
        Category category = new Category("category", "color", "image", "");
        categoryRepository.save(category);
        Product product = new Product("Test", 1000, "test.jpg", category);
        Product savedProduct = productRepository.save(product);
        Wish wish = new Wish(savedMember, savedProduct);
        Wish savedWish = wishRepository.save(wish);
        Long wishID = savedWish.getId();

        wishRepository.deleteById(wishID);

        boolean exists = wishRepository.existsById(wishID);
        assertThat(exists).isFalse();
    }
}
