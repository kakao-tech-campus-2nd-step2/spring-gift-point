package gift.dto;

public class ProductResponse {
    private final String name;
    private final int price;
    private final String description;
    private final String imageUrl;

    public ProductResponse(String name, int price, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
