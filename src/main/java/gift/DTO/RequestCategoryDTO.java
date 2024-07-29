package gift.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "카테고리 요청 DTO")
public record RequestCategoryDTO(
        @NotBlank
        @Schema(description = "카테고리 이름")
        String name,

        @NotBlank
        @Size(min=7, max=7, message = "카테고리 색상 값의 길이는 7입니다")
        @Schema(description = "카테고리 color")
        String color,

        @NotBlank
        @Schema(description = "카테고리 imageUrl")
        String imageUrl,

        @NotNull
        String description
){ }
