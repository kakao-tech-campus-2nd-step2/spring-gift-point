package gift.dto.product;

import gift.domain.Product;

public class ProductByCategoryResponse {
    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final Long categoryId;


    public ProductByCategoryResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.categoryId = product.getCategory().getId();
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

}
