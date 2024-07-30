package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 응답 DTO")
public class CategoryResponseDto {
    @Schema(description = "카테고리 고유 id ")
    private final Long id;
    @Schema(description = "카테고리 이름", defaultValue = "카테고리1", allowableValues = {"의류", "음식", "기념일", "옷", "쿠폰"})
    private final String name;
    @Schema(description = "색상", defaultValue = "색상1")
    private final String color;
    @Schema(description = "카테고리 상세 설명")
    private final String description;
    @Schema(description = "카테고리 이미지 url")
    private final String imageUrl;

    public CategoryResponseDto(Long id, String name, String color, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.description = description;
        this.imageUrl = imageUrl;
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

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
