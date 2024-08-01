package gift.web.dto.response.product;

import gift.domain.Product;
import gift.web.dto.response.category.ReadCategoryResponse;

public class UpdateProductResponse {

    private final Long id;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final Long categoryId;

    private UpdateProductResponse(Long id, String name, Integer price, String imageUrl, Long categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public static UpdateProductResponse from(Product product) {
        return new UpdateProductResponse(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl().toString(), product.getCategory().getId());
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
}
