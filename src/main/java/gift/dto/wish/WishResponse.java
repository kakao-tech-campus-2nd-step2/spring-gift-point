package gift.dto.wish;

import gift.model.wish.Wish;

public class WishResponse {
    public record Info(
            Long wishId,
            Long productId,
            String productName,
            int price,
            String imageUrl
    ){
        public static Info fromEntity(Wish wish){
            return new Info(wish.getId(),wish.getProduct().getId(),wish.getProduct().getName(),wish.getProduct().getPrice(),wish.getProduct().getImageUrl());
        }
    }
}
