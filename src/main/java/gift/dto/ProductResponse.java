package gift.dto;

import gift.entity.Product;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final long price;
    private final String imageUrl;
    private final Long categoryId;
    private final String categoryName;

    public ProductResponse(Long id, String name, long price, String imageUrl, Long categoryId,
        String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public ProductResponse(Product p) {
        this(p.getId(), p.getName(), p.getPrice(), p.getImageUrl(), p.getCategory().getId(),
            p.getCategory().getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
