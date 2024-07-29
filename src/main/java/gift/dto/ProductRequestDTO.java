package gift.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductRequestDTO(
                         @Size (max=15, message = "이름이 15글자를 초과하였습니다.")
                         @Pattern(
                                 regexp = "^[a-zA-Z0-9-ㄱ-하-ㅣ()\\[\\]+\\-\\&/_\\s]*$",
                                 message = "이름에 허용되지 않은 특수 문자가 포함되었습니다."
                         )
                         String name,
                         Long price,
                         String imageUrl,
                         Long categoryId) { }