package gift.dto.product;

import gift.dto.option.OptionRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public class ProductRequest {

    public record Create(
            @Size(max = 15)
            @Pattern(regexp = "[\\s\\(\\)\\[\\]\\+\\-&/_a-zA-Z0-9\uAC00-\uD7AF]*", message = "특수문자 오류")
            String name,

            Integer price,

            String imageUrl,

            Long categoryId,
            @NotEmpty(message = "옵션은 최소 하나 이상 포함되어야 합니다.")
            @Valid
            List<OptionRequest.Create> options
    ) {

    }

    public record Update(
            @Size(max = 15)
            @Pattern(regexp = "[\\s\\(\\)\\[\\]\\+\\-&/_a-zA-Z0-9\uAC00-\uD7AF]*", message = "특수문자 오류")
            String name,

            int price,

            String imageUrl,

            Long categoryId
    ) {

    }
}

