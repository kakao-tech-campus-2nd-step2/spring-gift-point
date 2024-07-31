package gift.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    protected Option() {
    }

    public Option(Product product, String name, Integer quantity) {
        this.product = product;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public synchronized void add(Integer quantity) {
        this.quantity += quantity;
    }

    public synchronized void subtract(Integer quantity) {
        this.quantity -= quantity;
    }

    @Override
    public String toString() {
        return "Option{" +
            "id=" + id +
            ", product=" + product +
            ", name='" + name + '\'' +
            ", quantity=" + quantity +
            '}';
    }
}
