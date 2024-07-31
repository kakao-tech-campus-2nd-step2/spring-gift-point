package gift.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CategoryUpdateRequest(
    @NotBlank(message = "카테고리를 입력해주세요.")
    @Length(min=1, max=20, message = "1~20자 사이로 입력해주세요.")
    String name,
    @NotBlank(message = "색상을 입력해주세요.")
    String color,
    @NotBlank(message = "이미지 주소를 입력해주세요.")
    @JsonProperty(value = "image_url")
    String imageUrl,
    @NotBlank(message = "설명을 입력해주세요.")
    String description
) {

}
