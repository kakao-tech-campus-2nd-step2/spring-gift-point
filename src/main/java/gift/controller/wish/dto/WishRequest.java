package gift.controller.wish.dto;

import gift.application.wish.dto.WishCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class WishRequest {

    public record Register(
        @NotNull
        Long productId,
        @Min(1)
        Long count
    ) {

        public WishCommand.Register toCommand() {
            return new WishCommand.Register(productId, count);
        }
    }

    public record Update(
        @NotNull
        Long productId,
        @Min(1)
        Long count
    ) {

        public WishCommand.Update toCommand() {
            return new WishCommand.Update(productId, count);
        }
    }

}
