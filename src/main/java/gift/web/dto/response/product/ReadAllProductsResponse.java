package gift.web.dto.response.product;

import java.util.List;

public class ReadAllProductsResponse {

    private List<ReadProductResponse> products;

    private ReadAllProductsResponse(List<ReadProductResponse> products) {
        this.products = products;
    }

    public static ReadAllProductsResponse from(List<ReadProductResponse> products) {
        return new ReadAllProductsResponse(products);
    }

    public List<ReadProductResponse> getProducts() {
        return products;
    }
}
