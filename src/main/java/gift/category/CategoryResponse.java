package gift.category;

public record CategoryResponse (Long id, String name, String color, String imageUrl, String description){
    public CategoryResponse(Category category) {
        this(category.getId(), category.getName(), category.getColor(), category.getImageUrl(), category.getDescription());
    }
}
