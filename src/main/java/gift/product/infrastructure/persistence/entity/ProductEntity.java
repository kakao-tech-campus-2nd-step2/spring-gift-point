package gift.product.infrastructure.persistence.entity;

import gift.core.BaseEntity;
import gift.core.domain.product.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class ProductEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategoryEntity category;

    protected ProductEntity() {
    }

    protected ProductEntity(
            Long id,
            String name,
            Integer price,
            String imageUrl,
            ProductCategoryEntity category
    ) {
        super(id);
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    protected ProductEntity(
            String name,
            Integer price,
            String imageUrl,
            ProductCategoryEntity category
    ) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public static ProductEntity fromDomain(Product product) {
        return new ProductEntity(
                product.id(),
                product.name(),
                product.price(),
                product.imageUrl(),
                ProductCategoryEntity.fromDomain(product.category())
        );
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

    public Product toDomain() {
        return new Product(getId(), name, price, imageUrl, category.toDomain());
    }
}
