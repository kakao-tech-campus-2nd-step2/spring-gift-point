package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record WishedProductDTO(
    long id,

    @Email(message = "이메일 양식에 맞지 않습니다.")
    String memberEmail,

    @NotNull
    long productId,

    @PositiveOrZero
    int amount
) {

}
