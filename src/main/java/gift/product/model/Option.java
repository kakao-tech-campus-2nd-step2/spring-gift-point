package gift.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(nullable = false)
    private final String name;
    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Product product;
    @Column(nullable = false)
    private final int quantity;

    protected Option() {
        this(null, null, 0, null);
    }

    public Option(String name, int quantity, Product product) {
        this(null, name, quantity, product);
    }

    public Option(Long id, String name, int quantity, Product product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Option subtract(int amount) {
        int amountResult = this.quantity - amount;

        if (amountResult < 0) {
            throw new IllegalArgumentException("차감 가능한 최대 옵션 수량을 초과하였습니다.");
        }

        return new Option(id, name, amountResult, product);
    }
}
