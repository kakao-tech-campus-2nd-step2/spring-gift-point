package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for creating or updating a Category")
public record CategoryRequest(
    @Schema(description = "Name of the category", example = "굿즈") String name,
    @Schema(description = "Color of the category", example = "#6c95d2") String color,
    @Schema(description = "Image URL of the category", example = "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png") String imageUrl,
    @Schema(description = "Description of the category", example = "굿즈 카테고리 입니다.") String description) {

}
