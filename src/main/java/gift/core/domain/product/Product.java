package gift.core.domain.product;

import jakarta.annotation.Nullable;

public class Product {
    private final Long id;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final ProductCategory category;

    public Product(
            Long id,
            String name,
            Integer price,
            String imageUrl,
            ProductCategory category
    ){
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Integer price() {
        return price;
    }

    public String imageUrl() {
        return imageUrl;
    }

    public String categoryName() {
        return category.name();
    }

    public ProductCategory category() {
        return category;
    }

    public Product applyUpdate(
            @Nullable String name,
            @Nullable Integer price,
            @Nullable String imageUrl,
            @Nullable ProductCategory category
    ){
        return new Product(
                this.id(),
                name != null ? name : this.name,
                price != null ? price : this.price,
                imageUrl != null ? imageUrl : this.imageUrl,
                category != null ? category : this.category
        );
    }

    public Product withCategory(ProductCategory category){
        return new Product(
                this.id(),
                this.name(),
                this.price(),
                this.imageUrl(),
                category
        );
    }
}
