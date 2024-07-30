package gift.domain;

import gift.utils.TimeStamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "option")
public class Option extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    @OneToMany(mappedBy = "option")
    private List<Order> orders = new ArrayList<>();

    public Option(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Option() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void subtract(int amount) {
        if (this.quantity < amount) {
            throw new IllegalStateException("Not enough quantity available");
        }
        this.quantity -= amount;
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setOption(null);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setOption(null);
    }

}
