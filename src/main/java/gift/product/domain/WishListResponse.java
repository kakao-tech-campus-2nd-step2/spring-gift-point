package gift.product.domain;

import java.util.List;

public class WishListResponse {
    private Long id;
    private List<ProductResponse> products;

    public WishListResponse(Long id, List<ProductResponse> products) {
        this.id = id;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }
}

