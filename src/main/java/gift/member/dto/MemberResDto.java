package gift.member.dto;

import gift.member.entity.Member;
import gift.wishlist.dto.WishListResDto;
import java.util.List;

public record MemberResDto(
        Long id,
        String email,
        List<WishListResDto> wishLists
) {

    public MemberResDto(Member member) {
        this(member.getId(), member.getEmail(), member.getWishLists().stream()
                .map(WishListResDto::new)
                .toList());
    }
}
