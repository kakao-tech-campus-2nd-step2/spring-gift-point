package gift.domain;

import gift.dto.request.OptionRequest;
import gift.exception.InsufficientQuantityException;
import jakarta.persistence.*;

import static gift.exception.ErrorCode.INSUFFICIENT_QUANTITY;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Option(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Option(OptionRequest optionRequest){
        this(optionRequest.getName(), optionRequest.getQuantity());
    }

    public Option() {
    }

    public void setProduct(Product product) {
        this.product = product;
        product.getOptions().add(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void subtractQuantity(Integer quantity){
        if (this.quantity < quantity) {
            throw new InsufficientQuantityException(INSUFFICIENT_QUANTITY);
        }
        this.quantity -= quantity;
    }

    public Product getProduct() {
        return product;
    }
}
