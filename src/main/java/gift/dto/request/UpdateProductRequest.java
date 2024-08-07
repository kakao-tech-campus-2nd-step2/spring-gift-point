package gift.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import static gift.constant.ErrorMessage.*;

public record UpdateProductRequest(

        @Size(max = 15, message = LENGTH_ERROR_MSG)
        @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = SPECIAL_CHAR_ERROR_MSG)
        @Pattern(regexp = "^(?!.*카카오).*$", message = KAKAO_CONTAIN_ERROR_MSG)
        String name,

        @Positive(message = POSITIVE_NUMBER_REQUIRED_MSG)
        Integer price,

        String imageUrl, String category) {
}
