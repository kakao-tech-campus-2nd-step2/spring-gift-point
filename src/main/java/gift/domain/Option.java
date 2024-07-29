package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column
    @Positive(message = "quantity must be bigger than 0")
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    public Option() {
    }

    public Option(String name, Integer quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Option subtractQuantity(Integer quantity) {
        this.quantity -= quantity;
        return this;
    }

    public UUID getProductId() {
        return product.getId();
    }

    public void updateDetails(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}