package gift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CategoryRequest(
        Long id,
        @NotBlank
        String name,
        @NotBlank
        @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "색상 코드가 적절하지 않습니다.")
        String color,
        @NotBlank
        @Pattern(regexp = "https://.*$", message = "이미지 주소가 적절하지 않습니다.")
        String imageUrl,
        @NotBlank
        String description
) {
}
