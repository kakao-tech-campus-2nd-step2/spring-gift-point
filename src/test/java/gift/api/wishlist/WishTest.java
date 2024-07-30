package gift.api.wishlist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.mock;

import gift.api.member.domain.Member;
import gift.api.product.domain.Product;
import gift.api.wishlist.domain.Wish;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WishTest {

    @Test
    void updateQuantity() {
        // given
        Member member = mock(Member.class);
        Product product = mock(Product.class);
        Wish wish = new Wish(member, product, 3);
        int expected = 7;

        // when
        wish.updateQuantity(expected);

        // then
        assertThat(wish.getQuantity()).isEqualTo(expected);
    }
}