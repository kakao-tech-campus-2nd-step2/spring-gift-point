package gift.product.domain;

public class WishListResponse {
    private Long id;
    private ProductResponse product;

    public WishListResponse(Long id, WishListProduct product) {
        this.id = id;
        this.product = new ProductResponse(product.getProduct());
    }

    public Long getId() {
        return id;
    }

    public ProductResponse getProduct() {
        return product;
    }
}

