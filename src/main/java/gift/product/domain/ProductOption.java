package gift.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    protected ProductOption() {
    }

    public ProductOption(Long id, String name, Integer quantity, Product product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public ProductOption(String name, Integer quantity, Product product) {
        this(null, name, quantity, product);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void modify(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public boolean isSameName(String name) {
        return this.getName().equals(name);
    }

    public void buy(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("Not enough quantity");
        }
        this.quantity -= quantity;
    }
}
