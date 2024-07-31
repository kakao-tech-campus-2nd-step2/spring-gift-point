package gift.entity;

import gift.dto.ProductDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "productEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishListEntity> wishListEntities;

    @ManyToOne(targetEntity = CategoryEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionEntity> options;

    public ProductEntity() {}

    public ProductEntity(Long id, String name, int price, String imageUrl,
        CategoryEntity categoryEntity, List<OptionEntity> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = categoryEntity;
        this.options = options;
    }

    public ProductEntity(Long id, String name, int price, String imageUrl, CategoryEntity category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public ProductEntity(String name, int price, String imageUrl, CategoryEntity category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public ProductEntity(String name, int price, String imageUrl, CategoryEntity category, List<OptionEntity> options) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.options = options != null ? options : new ArrayList<>();
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

    public String getImageUrl(){
        return imageUrl;
    }

    public List<WishListEntity> getWishListEntities() {
        return wishListEntities;
    }

    public CategoryEntity getCategory() {return category;}

    public List<OptionEntity> getOptions() {
        return options;
    }

    public static ProductDTO toDTO(ProductEntity productEntity) {
        return new ProductDTO(
            productEntity.getId(),
            productEntity.getName(),
            productEntity.getPrice(),
            productEntity.getImageUrl(),
            productEntity.getCategory() != null ? productEntity.getCategory().getId() : null,
            productEntity.getOptions() != null ? productEntity.getOptions().stream().map(OptionEntity::getId).collect(Collectors.toList()) : new ArrayList<>()
        );
    }

}