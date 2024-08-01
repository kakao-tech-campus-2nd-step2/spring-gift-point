package gift.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(
    @NotBlank(message = "카테고리 이름을 입력하세요.")
    String name
) {

}
