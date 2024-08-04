package gift.dto.requestdto;

import gift.domain.Option;
import gift.domain.Order;
import gift.domain.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequestDTO(
    @NotNull
    Long optionId,
    @NotNull
    @Min(1)
    int quantity,
    @NotBlank
    String message,
    @Min(0)
    int point) {

    public Order toEntity(User user, Option option){
        return new Order(user, option, quantity, message);
    }
}
