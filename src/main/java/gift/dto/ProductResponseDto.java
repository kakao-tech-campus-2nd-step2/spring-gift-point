package gift.dto;

public class ProductResponseDto {
    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final CategoryResponseDto category;

    public ProductResponseDto(Long id, String name, int price, String imageUrl, CategoryResponseDto category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public CategoryResponseDto getCategory() {
        return category;
    }
}
