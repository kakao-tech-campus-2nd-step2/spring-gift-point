package gift.model;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "product_id"})
})
public class Option extends BasicEntity{
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Option() {}

    public Option(Long id, String name) {
        super(id);
        this.name = name;
    }

    public Option(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Option(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public void updateOption(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public int subtractQuantity(int amount) {
        if (amount > quantity) {
            throw new IllegalArgumentException("Subtraction amount exceeds quantity");
        }
        quantity -= amount;
        return quantity;
    }


    public boolean isSameName(String theirName) {
        return name.equals(theirName);
    }

    public boolean isNotSameId(Long theirId) {
        return !getId().equals(theirId);
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
