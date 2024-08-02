package gift.domain;

import gift.dto.option.GetOptionResponse;
import gift.dto.option.OptionDto;
import gift.exception.InsufficientQuantityException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

@Entity
@Table(name = "option", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "product_id"})
})
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Version
    private Long version;

    protected Option() {

    }

    public Option(Long id, String name, int quantity, Product product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
        product.getOptions().add(this);
    }

    public Option(String name, int quantity, Product product) {
        this(null, name, quantity, product);
    }

    public void subtract(int quantity) {
        if (this.quantity < quantity) {
            throw new InsufficientQuantityException(this.quantity);
        }
        this.quantity -= quantity;
    }

    public void update(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public OptionDto toDto() {
        return new OptionDto(id, name, quantity);
    }

    public GetOptionResponse toGetOptionResponse() {
        return new GetOptionResponse(id, name, quantity);
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

}
