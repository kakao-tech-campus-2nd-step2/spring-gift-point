package gift.web.dto.response.product;

import gift.domain.Product;
import gift.web.dto.response.productoption.ReadProductOptionResponse;
import java.util.List;

public class CreateProductResponse {

    private final Long id;

    private final String name;

    private final Integer price;

    private final String imageUrl;

    private final List<ReadProductOptionResponse> options;

    public CreateProductResponse(Long id, String name, Integer price, String imageUrl,
        List<ReadProductOptionResponse> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<ReadProductOptionResponse> getOptions() {
        return options;
    }

    public static CreateProductResponse fromEntity(Product product) {
        return new CreateProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl().toString(),
            product.getProductOptions()
                .stream()
                .map(ReadProductOptionResponse::fromEntity)
                .toList());
    }
}
