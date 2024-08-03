package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.model.Category;
import gift.model.Member;
import gift.model.Option;
import gift.model.Product;
import gift.model.Wishlist;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class WishlistRepositoryTest {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OptionRepository optionRepository;

    private Member savedMember;
    private Product savedProduct;
    private Wishlist savedWishlist;
    private Wishlist saved;
    private Category savedCategory;
    private Option option;
    private Option savedOption;
    private Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    public void setUp() {
        Category category = new Category(1L, "교환권", "#007700", "임시 이미지", "임시 설명");
        savedCategory = categoryRepository.save(category);
        Member member = new Member(4L, "kbm@kbm.com", "mbk", "user");
        savedMember = memberRepository.save(member);
        Product product = new Product(1L, "상품", "100", savedCategory, "https://kakao");
        savedProduct = productRepository.save(product);
        Option option = new Option(1L, "옵션", 1L, savedProduct);
        savedOption = optionRepository.save(option);
        savedWishlist = new Wishlist(1L, savedMember, savedOption);
        saved = wishlistRepository.save(savedWishlist);
    }

    @Test
    public void testSaveWishlist() {
        assertAll(
            () -> assertThat(saved.getId()).isNotNull(),
            () -> assertThat(saved.getMember().getEmail()).isEqualTo("kbm@kbm.com"),
            () -> assertThat(saved.getOption().getId()).isEqualTo(1L)
        );
    }

    @Test
    public void testFindByMemberEmail() {
        Page<Wishlist> found = wishlistRepository.findByMember(savedMember, pageable);
        assertAll(
            () -> assertThat(found.getTotalElements()).isEqualTo(1),
            () -> assertThat(found.getContent().get(0).getMember().getEmail()).isEqualTo(
                savedMember.getEmail())
        );
    }

    @Test
    void testDeleteByOptionId() {
        wishlistRepository.deleteById(savedWishlist.getId());
        boolean exists = wishlistRepository.existsById(savedWishlist.getId());
        assertThat(exists).isFalse();
    }

    @Test
    void testDeleteByOptionIn() {
        Product product2 = new Product(null, "상품2", "200", savedCategory, "https://kakao");
        Product savedProduct2 = productRepository.save(product2);

        Option option2 = new Option(null, "옵션2", 2L, savedProduct2);
        Option savedOption2 = optionRepository.save(option2);
        Wishlist wishlist2 = new Wishlist(null, savedMember, option2);
        wishlistRepository.save(wishlist2);

        List<Option> options = List.of(savedOption, savedOption2);
        wishlistRepository.deleteByOptionIn(options);
        Page<Wishlist> result = wishlistRepository.findByMember(savedMember, pageable);
        assertThat(result.getTotalElements()).isEqualTo(0);
    }
}