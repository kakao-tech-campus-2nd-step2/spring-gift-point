package gift.option.entity;

import gift.product.entity.Product;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
    private long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name"))
    private Name name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "quantity"))
    private Quantity quantity;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    public long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public int getQuantity() {
        return quantity.getValue();
    }

    public Product getProduct() {
        return product;
    }

    protected Option() {

    }

    public Option(long id, String name, int quantity, Product product) {
        this.id = id;
        this.name = new Name(name);
        this.quantity = new Quantity(quantity);
        this.product = product;
    }

    public void update(String name, int quantity) {
        this.name.update(name);
        this.quantity.update(quantity);
    }

    public void subtract(int quantity) {
        this.quantity.subtract(quantity);
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Option option) {
            return id == option.id;
        }
        return false;
    }
}
