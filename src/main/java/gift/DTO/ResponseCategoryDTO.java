package gift.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 응답 DTO")
public class ResponseCategoryDTO {
    @Schema(description = "카테고리 Id")
    private Long id;

    @Schema(description = "카테고리 이름")
    private String name;

    @Schema(description = "카테고리 color")
    private String color;

    @Schema(description = "카테고리 imageUrl")
    private String imageUrl;

    @Schema(description = "카테고리 설명")
    private String description;

    public ResponseCategoryDTO() {
    }

    public ResponseCategoryDTO(Long id, String name, String color, String imageUrl, String description) {
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
