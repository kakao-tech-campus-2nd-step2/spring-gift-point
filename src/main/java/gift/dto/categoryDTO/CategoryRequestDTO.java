package gift.dto.categoryDTO;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO(
    @NotBlank(message = "카테고리 이름을 입력하세요")
    String name,
    @NotBlank(message = "카테고리 색상을 입력하세요")
    String color,
    @NotBlank(message = "이미지 URL을 입력하세요")
    String imageUrl,
    String description
) {

}