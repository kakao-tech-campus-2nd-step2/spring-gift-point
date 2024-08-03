package gift.category.dto;

import gift.category.entity.Category;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequestDto(
    @NotBlank(message = "항목 이름을 입력해야 합니다.")
    @Size(max = 15, message = "이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
    String name,

    @NotBlank(message = "색상을 입력해야 합니다.")
    String color,

    @NotBlank(message = "이미지 URL을 입력해야 합니다.")
    String imageUrl,

    String description) {

}
