package gift.entity;

import gift.domain.Option;
import jakarta.persistence.*;

@Entity
@Table(name = "options")
public class OptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    public OptionEntity() {

    }

    public OptionEntity(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public OptionEntity(String name, Long quantity, ProductEntity productEntity) {
        this.name = name;
        this.quantity = quantity;
        this.productEntity = productEntity;
    }

    public void updateOptionEntity(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void subtractQuantity(Long amountToSubtract) {
        if(amountToSubtract < 0){
            throw new IllegalArgumentException("Input value should to be positive");
        }
        this.quantity-=amountToSubtract;
    }

}
