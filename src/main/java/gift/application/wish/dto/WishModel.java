package gift.application.wish.dto;

import gift.model.wish.Wish;

public class WishModel {

    public record Info(
        Long wishId,
        Long productId,
        String productName,
        Integer price,
        String imageUrl,
        String category,
        String memberName,
        Long count
    ) {

        public static Info from(Wish wish) {
            return new Info(
                wish.getId(),
                wish.getProduct().getId(),
                wish.getProduct().getName(),
                wish.getProduct().getPrice(),
                wish.getProduct().getImageUrl(),
                wish.getProduct().getCategory().getName(),
                wish.getMember().getName(),
                wish.getCount()
            );
        }
    }


}
