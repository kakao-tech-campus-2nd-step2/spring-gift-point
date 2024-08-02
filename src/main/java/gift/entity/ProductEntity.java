package gift.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "product")
@EntityListeners(value = {AuditingEntityListener.class, gift.entity.listener.ProductEntityListener.class})
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(targetEntity = CategoryEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "productEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishListEntity> wishListEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "productEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOptionEntity> productOptions;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public ProductEntity() {
    }

    public ProductEntity(String name, long price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public List<WishListEntity> getWishListEntities() {
        return wishListEntities;
    }

    public void setWishListEntities(List<WishListEntity> wishListEntities) {
        this.wishListEntities = wishListEntities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<ProductOptionEntity> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(List<ProductOptionEntity> productOptions) {
        this.productOptions = productOptions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
