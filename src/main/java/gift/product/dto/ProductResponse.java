package gift.product.dto;

import gift.product.entity.Product;
import org.springframework.data.domain.Page;

public class ProductResponse {

    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private String categoryName;

    public ProductResponse() {
    }

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.categoryName = product.getCategory().getName();
    }

    public ProductResponse(Page<Product> products) {
    }
}
