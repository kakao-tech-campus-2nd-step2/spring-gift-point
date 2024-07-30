package gift.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Entity
@Table(name = "options", uniqueConstraints = @UniqueConstraint(columnNames = {"product_id",
    "name"}))
public class Option {

    // 상품 하나에 여러개의 옵션이 대응된다. 즉, 상품과 옵션의 관계는 일대다 매핑관계!
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false)

    private String name;
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Option() {

    }

    public Option(String name, long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Option(String name, long quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        setProduct(product);
    }

    public void subtract(long quantity) {
        this.quantity = Math.max(0, this.quantity - quantity);
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

    public Long getQuantity() {
        return quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null && !product.contains(this)) {
            product.add(this);
        }
    }

    public void update(String name, long quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}
