package gift.dto.categoryDto;

public class CategoryResponseDto {
    private Long id;
    private String name;
    private String color;
    private String imageUrl;
    private String description;

    public CategoryResponseDto(Long id, String name, String color, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
