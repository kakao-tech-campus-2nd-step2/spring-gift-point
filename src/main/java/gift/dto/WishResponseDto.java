package gift.dto;

import gift.domain.Wish;

public record WishResponseDto(Long id, String member_email, String product_name,
                              int product_price) {

    public static WishResponseDto convertToDto(Wish wish) {
        return new WishResponseDto(
            wish.getId(),
            wish.getMember().getEmail(),
            wish.getProduct().getName(),
            wish.getProduct().getPrice());
    }
}
