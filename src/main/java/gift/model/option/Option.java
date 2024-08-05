package gift.model.option;

import gift.exception.OutOfStockException;
import gift.model.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "product_option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String name;

    @NotNull
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Version
    @ColumnDefault("0")
    private Integer version;

    protected Option() {
    }

    public Option(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public Option(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getGift() {
        return product;
    }

    public void setGift(Product product) {
        this.product = product;
    }

    public void modify(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void modify(String name) {
        this.name = name;
    }

    public void modify(int quantity) {
        this.quantity = quantity;
    }

    public void subtract(int quantity) {
        if (this.quantity < quantity) {
            throw new OutOfStockException("재고가 부족합니다.");
        }
        this.quantity -= quantity;
    }
}
