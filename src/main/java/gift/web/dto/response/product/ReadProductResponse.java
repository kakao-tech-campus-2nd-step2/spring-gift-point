package gift.web.dto.response.product;

import gift.domain.Product;
import gift.web.dto.response.category.ReadCategoryResponse;
import gift.web.dto.response.productoption.ReadProductOptionResponse;
import java.util.List;

public class ReadProductResponse {

    private final Long id;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final List<ReadProductOptionResponse> options;
    private final ReadCategoryResponse category;

    public ReadProductResponse(Long id, String name, Integer price, String imageUrl,
        List<ReadProductOptionResponse> options, ReadCategoryResponse category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.options = options;
        this.category = category;
    }

    public static ReadProductResponse fromEntity(Product product) {
        List<ReadProductOptionResponse> productOptions = product.getProductOptions()
            .stream()
            .map(ReadProductOptionResponse::fromEntity)
            .toList();

        ReadCategoryResponse category = ReadCategoryResponse.fromEntity(product.getCategory());

        return new ReadProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl().toString(), productOptions, category);
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

    public ReadCategoryResponse getCategory() {
        return category;
    }

}
