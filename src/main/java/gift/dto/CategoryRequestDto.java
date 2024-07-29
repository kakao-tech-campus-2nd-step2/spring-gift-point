package gift.dto;

import jakarta.validation.constraints.NotEmpty;

public class CategoryRequestDto {

    @NotEmpty(message = "이미지 URL은 필수 입력 사항입니다.")
    private final String imageUrl;

    @NotEmpty(message = "색상은 필수 입력 사항입니다.")
    private final String color;

    @NotEmpty(message = "상품 설명은 필수 입력 사항입니다.")
    private final String description;

    @NotEmpty(message = "상품명은 필수 입력 사항입니다.")
    private final String name;

    public CategoryRequestDto(String name, String imageUrl, String color, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.color = color;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }
}