package gift.dto;

public class WishlistResponse {

    private Long id;
    private ProductResponse product;

    public WishlistResponse(Long id, ProductResponse product) {
        this.id = id;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
