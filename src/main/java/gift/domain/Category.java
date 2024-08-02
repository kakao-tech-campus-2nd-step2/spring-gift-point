package gift.domain;

import gift.entity.CategoryEntity;
import gift.entity.OptionEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.stream.Collectors;

public class Category {

    public record CategoryRequest(
        @NotBlank(message = "카테고리 이름을 반드시 입력해 주세요")
        String name,
        @NotBlank(message = "색깔의 HEX CODE를 반드시 입력해 주세요")
        @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "HEX CODE 형식에 맞추어 작성해 주세요")
        String color,
        @NotBlank(message = "이미지URL을 입력해 주세요.")
        @Pattern(regexp = "^(http(s?):)([/|.\\w|\\s|-])*\\.(?:jpg|gif|png)$", message = "URL 형식에 맞추어 작성해주세요")
        String imageUrl,
        @NotBlank(message = "상세 설명을 적어 주세요.")
        @Size(min = 1, max = 80, message = "1~80자 사이로 적어 주세요")
        String description
    ) {

    }

    public record CategoryResponse(
        Long id,
        String name,
        String color,
        String imageUrl,
        String description
    ) {
        public static CategoryResponse from(CategoryEntity categoryEntity) {
            return new CategoryResponse(
                categoryEntity.getId(),
                categoryEntity.getName(),
                categoryEntity.getColor(),
                categoryEntity.getImageUrl(),
                categoryEntity.getDescription()
            );
        }
    }

}
