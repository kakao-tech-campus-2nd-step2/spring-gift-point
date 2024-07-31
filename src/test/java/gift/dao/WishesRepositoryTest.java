package gift.dao;

import gift.product.dao.OptionRepository;
import gift.product.dao.ProductRepository;
import gift.product.entity.Category;
import gift.member.dao.MemberRepository;
import gift.member.entity.Member;
import gift.product.entity.Option;
import gift.product.entity.Product;
import gift.wishlist.dao.WishesRepository;
import gift.wishlist.entity.Wish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import testFixtures.CategoryFixture;
import testFixtures.MemberFixture;
import testFixtures.OptionFixture;
import testFixtures.ProductFixture;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class WishesRepositoryTest {

    @Autowired
    private WishesRepository wishesRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        Category category = CategoryFixture.createCategory("상품권");
        product = ProductFixture.createProduct("product", category);
        product = productRepository.save(product);
    }

    @Test
    @DisplayName("위시 추가 및 ID 조회 테스트")
    void saveAndFindById() {
        Member member = MemberFixture.createMember("test@email.com");
        member = memberRepository.save(member);

        Option option = OptionFixture.createOption("옵션", product);
        Option savedOption = optionRepository.save(option);

        Wish wish = new Wish(member, savedOption);
        Wish savedWish = wishesRepository.save(wish);

        Wish foundWish = wishesRepository.findById(savedWish.getId())
                .orElse(null);

        assertThat(foundWish).isNotNull();
        assertThat(foundWish.getMember()).isEqualTo(savedWish.getMember());
        assertThat(foundWish.getOption()).isEqualTo(savedWish.getOption());
    }

    @Test
    @DisplayName("위시 ID 조회 실패 테스트")
    void findByIdFailed() {
        Member member = MemberFixture.createMember("test@email.com");
        Member savedMember = memberRepository.save(member);

        Option option = OptionFixture.createOption("옵션", product);
        Option savedOption = optionRepository.save(option);

        Wish wish = new Wish(savedMember, savedOption);
        wishesRepository.save(wish);

        Wish foundWish = wishesRepository.findById(123456789L)
                .orElse(null);

        assertThat(foundWish).isNull();
    }

    @Test
    @DisplayName("위시 회원 ID 조회 테스트")
    void findByMemberId() {
        Member member1 = MemberFixture.createMember("test1@email.com");
        member1 = memberRepository.save(member1);
        Member member2 = MemberFixture.createMember("test2@email.com");
        member2 = memberRepository.save(member2);

        Option option1 = OptionFixture.createOption("옵션1", product);
        option1 = optionRepository.save(option1);
        Option option2 = OptionFixture.createOption("옵션2", product);
        option2 = optionRepository.save(option2);

        Wish wish1 = new Wish(member1, option1);
        Wish wish2 = new Wish(member2, option2);
        Wish wish3 = new Wish(member1, option2);
        wishesRepository.save(wish1);
        wishesRepository.save(wish2);
        wishesRepository.save(wish3);

        Page<Wish> wishesPage = wishesRepository.findByMember_Id(member1.getId(), PageRequest.of(0, 10));

        assertThat(wishesPage.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("위시 회원 ID 조회 실패 테스트")
    void findByMemberIdFailed() {
        Member member1 = MemberFixture.createMember("test1@email.com");
        member1 = memberRepository.save(member1);
        Member member2 = MemberFixture.createMember("test2@email.com");
        member2 = memberRepository.save(member2);

        Option option1 = OptionFixture.createOption("옵션1", product);
        option1 = optionRepository.save(option1);
        Option option2 = OptionFixture.createOption("옵션2", product);
        option2 = optionRepository.save(option2);;

        Wish wish1 = new Wish(member1, option1);
        Wish wish2 = new Wish(member2, option2);
        Wish wish3 = new Wish(member1, option2);
        wishesRepository.save(wish1);
        wishesRepository.save(wish2);
        wishesRepository.save(wish3);

        Page<Wish> wishPage = wishesRepository.findByMember_Id(987654321L, PageRequest.of(0, 10));

        assertThat(wishPage).isEmpty();
    }

    @Test
    @DisplayName("위시 삭제 테스트")
    void deleteWish() {
        Member member = MemberFixture.createMember("test@email.com");;
        member = memberRepository.save(member);

        Option option = OptionFixture.createOption("옵션", product);
        option = optionRepository.save(option);

        Wish wish = new Wish(member, option);
        Wish savedWish = wishesRepository.save(wish);

        wishesRepository.deleteById(savedWish.getId());

        boolean exists = wishesRepository.existsById(savedWish.getId());
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("회원 ID 및 상품 ID로 위시 조회 테스트")
    void findByMember_IdAndProduct_Id() {
        Member member = MemberFixture.createMember("test@email.com");;
        member = memberRepository.save(member);

        Option option = OptionFixture.createOption("옵션", product);
        option = optionRepository.save(option);

        Wish wish = new Wish(member, option);
        wishesRepository.save(wish);

        Wish foundWish = wishesRepository.findByMember_IdAndOption_Id(member.getId(), product.getId())
                .orElse(null);

        assertThat(foundWish).isNotNull();
        assertThat(foundWish.getMember()
                .getEmail()).isEqualTo(member.getEmail());
        assertThat(foundWish.getOption()
                .getName()).isEqualTo(option.getName());
    }

    @Test
    @DisplayName("회원 ID 및 상품 ID로 위시 존재 여부 실패 확인 테스트")
    void findByMember_IdAndProduct_IdFailed() {
        Member member = MemberFixture.createMember("test@email.com");;
        member = memberRepository.save(member);

        Option option = OptionFixture.createOption("옵션", product);
        option = optionRepository.save(option);

        Wish wish = new Wish(member, option);
        wishesRepository.save(wish);

        Wish foundWish = wishesRepository.findByMember_IdAndOption_Id(12345L, 67890L)
                .orElse(null);

        assertThat(foundWish).isNull();
    }

}