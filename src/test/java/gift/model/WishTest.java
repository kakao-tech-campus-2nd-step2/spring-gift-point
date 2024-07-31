package gift.model;

import gift.common.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class WishTest {

    @Test
    @DisplayName("Wish의 member 확인[실패] - wish의 member 불일치")
    void checkWishByMemberId() {
        // given
        Long memberId = 1L;
        Long memberId2 = 2L;
        Member member = new Member(memberId, null, null, null, null, null);
        List<Option> options = List.of(new Option("oName", 123));
        Product product = new Product(null, 0, null, null, options);
        Wish wish = new Wish(member, 0, product);
        // when
        // then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> wish.checkWishByMemberId(memberId2));
    }

    @Test
    @DisplayName("Wish의 Product 확인[실패] - wish의 product id 불일치")
    void checkWishByProductId() {
        // given
        Long productId = 1L;
        Long productId2 = 2L;
        Member member = new Member(null, null, null);
        Product product = new Product(productId);
        Wish wish = new Wish(member, 0, product);

        // when
        // then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> wish.checkWishByProductId(productId2));
    }
}