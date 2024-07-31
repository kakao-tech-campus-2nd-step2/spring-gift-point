package gift.Model.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="options")
public class OptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="product_id")
    private ProductEntity product;

    @Column(name="optionName")
    private String name;

    @Column(name="quantity")
    private Long quantity;

    public OptionEntity(){}

    public OptionEntity(ProductEntity product, String name, Long quantity){
        this.product = product;
        this.name = name;
        this.quantity = quantity;
    }

    public boolean subtract(Long count){
        if(this.quantity - count > 0) {
            this.quantity = this.quantity - count;
            return true;
        }

        throw new IllegalArgumentException("수량이 부족합니다.");
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
