package gift.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CategoryDTO(
        Long id,

        @NotNull(message = "이름은 null이 될 수 없습니다.")
        @Size(min = 1, max = 15, message = "이름은 1자에서 15자 사이여야 합니다.")
        @Pattern(regexp = "^[\\p{L}\\p{N}\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$", message = "이름은 문자, 숫자, 공백 및 특수 문자 ( ) [ ] + - & / _ 만 포함할 수 있습니다.")
        @Pattern(regexp = "^(?!.*카카오).*$", message = "카카오가 들어가는 품목명은 담당 MD와 협의한 경우에만 사용 가능합니다.")
        String name,

        @NotNull(message = "설명은 null이 될 수 없습니다.")
        String description,
        @NotNull(message = "색상은 null이 될 수 없습니다.")
        @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "색상은 16진수 6자리여야 합니다.")
        String color,

        @NotNull(message = "이미지 URL은 null이 될 수 없습니다.")
        @Pattern(regexp = "https?://.*", message = "이미지 URL은 http 또는 https로 시작해야 합니다.")
        String imageUrl


) {}
