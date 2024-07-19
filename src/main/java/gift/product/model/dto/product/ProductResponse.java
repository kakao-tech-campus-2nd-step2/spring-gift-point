package gift.product.model.dto.product;

import gift.product.model.dto.option.OptionResponse;
import java.util.List;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final Long categoryId;
    private final Long wishCount;
    private final List<OptionResponse> options;
    private final Long sellerId;


    public ProductResponse(Product product, List<OptionResponse> options, Long wishCount) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.categoryId = product.getCategory().getId();
        this.wishCount = wishCount;
        this.options = options;
        this.sellerId = product.getSeller().getId();
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

    public Long getCategoryId() {
        return categoryId;
    }

    public Long getWishCount() {
        return wishCount;
    }

    public List<OptionResponse> getOptions() {
        return options;
    }

    public Long getSellerId() {
        return sellerId;
    }
}
