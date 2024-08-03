package gift.dto.wishedProduct;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record WishedProductDto(
    long id,

    @Email(message = "이메일 양식에 맞지 않습니다.")
    String memberEmail,

    @NotNull
    long productId
) {

}
