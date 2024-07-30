package gift.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity(name = "product_option")
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    public ProductOption(String name, Long quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        product.addProductOption(this);
    }

    public ProductOption() {
    }

    public Product getProduct() {
        return product;
    }

    public Long getId() {
        return id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public void decreaseQuantity(Long quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.quantity -= quantity;
    }

}
