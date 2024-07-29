package gift.entity;


import gift.domain.Product;
import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL)
    private List<WishListEntity> wishListEntities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity categoryEntity;

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionEntity> optionEntities;

    public ProductEntity() {

    }

    public ProductEntity(String name, Long price, String imageUrl, CategoryEntity categoryEntity, List<OptionEntity> optionEntities) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryEntity = categoryEntity;
        this.optionEntities = optionEntities;
    }

    public ProductEntity(Long id, String name, Long price, String imageUrl, CategoryEntity categoryEntity, List<OptionEntity> optionEntities) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryEntity = categoryEntity;
        this.optionEntities = optionEntities;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<WishListEntity> getWishListEntities() {
        return wishListEntities;
    }

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public List<OptionEntity> getOptionEntities() {
        return optionEntities;
    }

    public static Product toDto(ProductEntity productEntity) {
        return new Product(
            productEntity.getId(),
            productEntity.getName(),
            productEntity.getPrice(),
            productEntity.getImageUrl(),
            productEntity.getCategoryEntity().getId(),
            productEntity.getOptionEntities().stream().map(OptionEntity::getId).collect(Collectors.toList())
        );
    }

}
