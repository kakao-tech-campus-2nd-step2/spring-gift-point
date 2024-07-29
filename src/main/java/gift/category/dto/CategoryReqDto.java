package gift.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryReqDto(
        @NotBlank(message = "카테고리 이름은 필수입니다.")
        String name,

        @NotBlank(message = "카테고리 색상은 필수입니다.")
        String color,

        @NotBlank(message = "카테고리 이미지 URL은 필수입니다.")
        String imageUrl,

        String description
) {
}
