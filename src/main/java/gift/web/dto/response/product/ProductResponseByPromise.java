package gift.web.dto.response.product;

import gift.domain.Product;
import gift.web.dto.response.productoption.ReadProductOptionResponse;
import java.util.List;

public class ProductResponseByPromise {

    private final Long id;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final Long categoryId;
    private final List<ReadProductOptionResponse> options;

    public ProductResponseByPromise(Long id, String name, Integer price, String imageUrl,
        List<ReadProductOptionResponse> options, Long categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.options = options;
        this.categoryId = categoryId;
    }

    public static ProductResponseByPromise fromEntity(Product product) {
        List<ReadProductOptionResponse> productOptions = product.getProductOptions()
            .stream()
            .map(ReadProductOptionResponse::fromEntity)
            .toList();

        return new ProductResponseByPromise(product.getId(), product.getName(), product.getPrice(), product.getImageUrl().toString(), productOptions, product.getCategory().getId());
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

    public Long getCategoryId() {
        return categoryId;
    }

}
