package gift.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(
        @NotBlank(message = "카테고리 이름을 입력해 주세요")
        String name,

        @NotBlank(message = "카테고리 색상을 입력해 주세요")
        String color
) { }
