package gift.dto.request;

import gift.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "카테고리 이름은 공백일 수 없습니다.")
        @Size(max = 15, message = "카테고리 이름은 15자 이하로 입력해주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = "사용할 수 없는 특수문자가 포함되어 있습니다.")
        String name,
        String color,
        String imageUrl,
        String description) {
    public Category toEntity(){
        return new Category(this.name, this.color, this.imageUrl, this.description);
    }
}
