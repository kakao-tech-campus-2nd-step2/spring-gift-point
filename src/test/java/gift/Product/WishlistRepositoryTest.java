package gift.Product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.model.Member;
import gift.model.WishList;
import gift.repository.MemberRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class WishlistRepositoryTest {
    @Autowired
    private WishlistRepository wishlistRepository;
    private MemberRepository memberRepository;

    @Autowired
    WishlistRepositoryTest(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Test
    void save() {
        Member member = new Member("example100@example.com","password");
        memberRepository.save(member);
        WishList expected = new WishList( member,111L);
        WishList actual = wishlistRepository.save(expected);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getMember()).isEqualTo(expected.getMember());
        assertThat(actual.getProductId()).isEqualTo(expected.getProductId());
    }

    @Test
    void findWishlistByMember() {
        Member member = new Member("example100@example.com","password");
        memberRepository.save(member);
        WishList expected = new WishList(member, 100L);

        WishList actual = wishlistRepository.save(expected);
        Pageable pageable = PageRequest.of(0, 5);
        Page<WishList> products = wishlistRepository.findByMember(member, pageable);
        assertThat(products.stream().findAny()).isPresent();
    }

    @Test
    void deleteById() {
        Member member = new Member("example100@example.com","password");
        memberRepository.save(member);
        WishList expected = new WishList(member, 100L);
        wishlistRepository.save(expected);
        wishlistRepository.deleteById(expected.getProductId());
        Optional<WishList> deletedWish = wishlistRepository.findById(expected.getProductId());
        assertThat(deletedWish).isNotPresent();

    }
}
