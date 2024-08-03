package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

public record ProductRequest(
        @NotBlank(message = "상품 이름은 공백일 수 없습니다.")
        @Size(max = 15, message = "상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
        @Pattern(regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-&/_\\uAC00-\\uD7A3\\u3131-\\u3163]*$", message = "상품 이름이 유효하지 않은 문자를 포함하고 있습니다.")
        String name,

        @Min(value = 0, message = "가격은 음수일 수 없습니다.")
        int price,

        @JsonProperty("image_url")
        String imageUrl,

        @JsonProperty("category_id")
        @Min(value = 1, message = "카테고리는 필수입니다.")
        Long categoryId,

        List<OptionRequest> options
) {
    public ProductRequest {
        Objects.requireNonNull(name, "상품 이름은 공백일 수 없습니다.");
        Objects.requireNonNull(imageUrl, "이미지 URL은 공백일 수 없습니다.");
        Objects.requireNonNull(categoryId, "카테고리 ID는 공백일 수 없습니다.");
    }
}
