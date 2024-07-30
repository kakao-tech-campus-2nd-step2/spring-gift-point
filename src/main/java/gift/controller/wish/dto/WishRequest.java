package gift.controller.wish.dto;

import gift.model.Product;
import gift.model.User;
import gift.model.Wish;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class WishRequest {

    public record Create(
        @NotNull
        Long productId
       ) {

        public Wish toEntity(User user, Product product) {
            return new Wish(user, product);
        }
    }

    public record Update(
        @Min(0) int count) {
    }
}
