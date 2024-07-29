package gift.domain.model.dto;

public class ProductResponseDto {

    private final Long id;
    private final String name;
    private final Long price;
    private final String imageUrl;
    private final String categoryName;

    public ProductResponseDto(Long id, String name, Long price, String imageUrl, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
