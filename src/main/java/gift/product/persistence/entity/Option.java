package gift.product.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "option",
    indexes = {@Index(name = "idx_product_id", columnList = "product_id")}
)
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer quantity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    public Option(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    protected Option() {

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

    public void update(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void subtractQuantity(Integer quantity) {
        this.quantity -= quantity;
    }
}
