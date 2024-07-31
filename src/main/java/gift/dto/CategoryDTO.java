package gift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CategoryDTO {
    private Long id;

    @NotBlank(message = "카테고리 이름은 필수 입력 항목입니다.")
    private String name;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "올바른 HEX 색상 코드로 입력해 주세요.")
    private String color;

    @Pattern(regexp = "^https?://.*$", message = "올바른 이미지 URL 형식으로 입력해 주세요.")
    private String imageUrl;

    @Size(max = 255, message = "설명은 최대 255글자 이하로 입력해 주세요.")
    private String description;

    public CategoryDTO(Long id, String name, String color, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
