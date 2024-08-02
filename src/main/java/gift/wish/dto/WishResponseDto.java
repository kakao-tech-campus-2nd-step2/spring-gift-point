package gift.wish.dto;

import gift.wish.entity.Wish;

public record WishResponseDto(
    Long id,
    Long productId,
    int count
) {

  public static WishResponseDto toDto(Wish wish){
    return new WishResponseDto(
        wish.getId(),
        wish.getProduct().getId(),
        wish.getCount()
    );
  }

}
