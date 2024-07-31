package gift.entity;

import jakarta.persistence.*;

@Entity
public class Option extends BaseEntity {
    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Option() {}

    public Option(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void subtractQuantity(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("Quantity to subtract exceeds available quantity");
        }
        this.quantity -= quantity;
    }
}
