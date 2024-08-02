package gift.entity;


import jakarta.persistence.*;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity categoryEntity;

    @OneToMany(mappedBy = "productEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionEntity> optionEntities;

    public ProductEntity() {

    }

    public ProductEntity(String name, Long price, String imageUrl, CategoryEntity categoryEntity, List<OptionEntity> optionEntities) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryEntity = categoryEntity;
        for(OptionEntity optionEntity : optionEntities) {
            this.optionEntities.add(optionEntity);
        }
    }

    public void updateProductEntity(String name, Long price, String imageUrl, CategoryEntity categoryEntity) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryEntity = categoryEntity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public List<OptionEntity> getOptionEntities() {
        return optionEntities;
    }

}
