package gift.domain.wishlist;

import gift.domain.member.Member;
import gift.domain.option.Option;
import jakarta.validation.constraints.Min;

public record WishListRequest(
    @Min(value = 0, message = "옵션 번호가 올바르지 않습니다")
    long optionId
) {

    public WishList toWishList(Long memberId) {
        return new WishList(new Member(memberId), new Option(optionId));
    }
}
