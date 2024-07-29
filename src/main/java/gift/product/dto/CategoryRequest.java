package gift.product.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CategoryRequest(
        @NotEmpty(message = "반드시 값이 존재해야 합니다.")
        String name,
        @NotNull
        @Pattern(regexp = "#[\\da-f]{6}", message = "HEX 색상 코드를 입력해야 합니다.")
        String color,
        @NotNull
        String imageUrl,
        String description) { }