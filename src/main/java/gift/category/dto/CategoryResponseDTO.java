package gift.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "카테고리 응답 DTO")
public class CategoryResponseDTO {

    @Schema(description = "카테고리 id")
    private Long id;

    @Schema(description = "카테고리명")
    private String name;

    @Schema(description = "카테고리 색상")
    private String color;

    @Schema(description = "카테고리 이미지 url")
    private String imageUrl;

    @Schema(description = "카테고리 설명")
    private String description;

    public CategoryResponseDTO() {
    }

    public CategoryResponseDTO(
        Long id,
        String name,
        String color,
        String imageUrl,
        String description
    ) {
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

    @Override
    public boolean equals(Object object) {
        if (object instanceof CategoryResponseDTO categoryResponseDTO) {
            return Objects.equals(id, categoryResponseDTO.id)
                   && Objects.equals(name, categoryResponseDTO.name)
                   && Objects.equals(color, categoryResponseDTO.color)
                   && Objects.equals(imageUrl, categoryResponseDTO.imageUrl)
                   && Objects.equals(description, categoryResponseDTO.description);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, imageUrl, description);
    }
}
