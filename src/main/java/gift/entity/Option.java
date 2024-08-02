package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Option() {
    }

    public Option(String name, int stockQuantity, Product product) {
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.product = product;
    }

    public Option(String name, int stockQuantity) {
        this.name = name;
        this.stockQuantity = stockQuantity;
    }

    public void subtractQuantity(int quantity) {
        this.stockQuantity -= quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void updateOption(String name, int stockQuantity) {
        this.name = name;
        this.stockQuantity = stockQuantity;
    }

}