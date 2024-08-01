package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "option", uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "name"}))
public class Option extends BaseEntity {
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Option() {
    }

    public Option(Long id, String name, int quantity, Product product) {
        super(id);
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Option(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public void subtract(int quantityToSubtract) {
        if (quantityToSubtract < 0) {
            throw new IllegalArgumentException("수량은 음수가 될 수 없습니다.");
        }
        if (this.quantity < quantityToSubtract) {
            throw new IllegalArgumentException("남아있는 수량이 더 작습니다.");
        }
        this.quantity -= quantityToSubtract;
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
}
