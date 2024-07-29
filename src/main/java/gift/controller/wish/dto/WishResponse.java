package gift.controller.wish.dto;

import gift.application.wish.dto.WishModel;

public class WishResponse {

    public record Info(
        Long wishId,
        Long productId,
        String productName,
        Long count
    ) {

        public static Info from(WishModel.Info model) {
            return new Info(
                model.wishId(),
                model.productId(),
                model.productName(),
                model.count()
            );
        }
    }
}
