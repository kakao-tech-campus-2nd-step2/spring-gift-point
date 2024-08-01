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

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false)
    private Long quantity;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    protected Option() {

    }

    public Option(String name, Long quantity, Product product) {
        if (name.length() > 50) {
            throw new IllegalArgumentException("Name is too long");
        }
        if (quantity < 1 || quantity >= 100_000_000)  {
            throw new IllegalArgumentException("Quantity is invalid");
        }

        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public void setProduct(Product product) {
        if (this.product != null && !this.product.equals(product)) {
            this.product.getOptionList().remove(this);
        }

        this.product = product;
        product.getOptionList().add(this);
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

    public void updateOption(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public boolean isSameName(Option option) {
        return this.name.equals(option.name);
    }

    public void subtractQuantity(Long quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Deduct Quantity value is invalid");
        }
        if(quantity > this.quantity) {
            throw new IllegalArgumentException("cannot subtract by more than quantity");
        }

        this.quantity -= quantity;
    }
}
