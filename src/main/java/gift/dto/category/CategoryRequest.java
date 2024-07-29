package gift.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CategoryRequest(
        @NotBlank(message = "이름의 길이는 최소 1자 이상이어야 합니다.")
        String name,
        @NotBlank(message = "카테고리 설명은 필수로 입력해야 합니다.")
        String description,
        @Pattern(regexp = "^\\#[0-9a-zA-Z]{6,8}$", message = "허용되지 않은 형식의 색상코드입니다.")
        String color,
        @NotBlank(message = "카테고리 설명 이미지는 필수로 입력해야 합니다.")
        String imageUrl
) {
}
