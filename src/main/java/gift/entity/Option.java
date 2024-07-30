package gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "option")
@Schema(description = "옵션")
public class Option {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Schema(description = "옵션 고유 id")
    private Long id;
    @Column(nullable = false)
    @Schema(description = "옵션 이름")
    private String name;
    @Column(nullable = false)
    @Schema(description = "옵션 개수")
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @Schema(description = "옵션이 속한 상품 id")
    private Product product;

    public Option() {
    }

    public Option(Long id, String name, Long quantity, Product product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Option(String name, Long quantity, Product product) {
        this(null, name, quantity, product);
    }

    public Option(String name, Long quantity) {
        this(null, name, quantity, null);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}