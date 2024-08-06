package gift.domain.option;

import gift.domain.product.Product;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long quantity;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Product product;

    public Option() {

    }

    public Option(Long id) {
        this.id = id;
    }

    public Option(String name, Long quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public void update(OptionRequest optionRequest) {
        this.name = optionRequest.name();
        this.quantity = optionRequest.quantity();
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

    public void subtractQuantity(Long quantity) {
        if (quantity <= this.quantity) {
            this.quantity -= quantity;
        }
    }
}
