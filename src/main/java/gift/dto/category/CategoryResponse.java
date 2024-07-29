package gift.dto.category;

public record CategoryResponse(Long id, String name, String description, String color, String imageUrl) {
    public static CategoryResponse of(Long id, String name, String description, String color, String imageUrl) {
        return new CategoryResponse(id, name, description, color, imageUrl);
    }
}
