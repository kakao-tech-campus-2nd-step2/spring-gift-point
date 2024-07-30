package gift.dto.gift;

import gift.dto.category.CategoryResponse;
import gift.dto.option.OptionResponse;
import gift.model.gift.Product;

import java.util.List;

public class ProductResponse {

    private final Long id;

    private final String name;

    private final int price;

    private final String imageUrl;

    private final CategoryResponse.Info category;

    private final List<OptionResponse> options;


    public ProductResponse(Long id, String name, int price, String imageUrl, CategoryResponse.Info category, List<OptionResponse> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.options = options;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                CategoryResponse.Info.fromEntity(product.getCategory()),
                product.getOptions().stream()
                        .map(OptionResponse::from)
                        .toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public CategoryResponse.Info getCategory() {
        return category;
    }

    public List<OptionResponse> getOptions() {
        return options;
    }
}
