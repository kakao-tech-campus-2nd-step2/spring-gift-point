package gift.dto;

public class ProductDto {

    private final Long id;
    private final String name;
    private final double price;
    private final String imageUrl;
    private final CategoryDto categoryDto;

    public ProductDto(Long id, String name, double price, String imageUrl,
        CategoryDto categoryDto) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryDto = categoryDto;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
