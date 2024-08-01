package gift.web.dto.response.product;

import gift.domain.Product;
import gift.web.dto.response.category.ReadCategoryResponse;
import gift.web.dto.response.productoption.ReadProductOptionResponse;
import java.util.List;

public class UpdateProductResponse {

    private final Long id;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final Long categoryId;
    private final List<ReadProductOptionResponse> options;


    private UpdateProductResponse(Long id, String name, Integer price, String imageUrl, Long categoryId, List<ReadProductOptionResponse> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.options = options;
    }

    public static UpdateProductResponse from(Product product) {
        return new UpdateProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl().toString(),
            product.getCategory().getId(),
            product.getProductOptions()
                .stream()
                .map(ReadProductOptionResponse::fromEntity)
                .toList());
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

    public Long getCategoryId() {
        return categoryId;
    }

    public List<ReadProductOptionResponse> getOptions() {
        return options;
    }
}
