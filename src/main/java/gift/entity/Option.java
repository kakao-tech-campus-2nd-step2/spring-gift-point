package gift.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;

@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long quantity;

    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    private Product product;

    private Option(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.quantity = builder.quantity;
        this.product = builder.product;
    }

    public Option() {}

    public void update(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
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

    public static class Builder {
        private Long id;
        private String name;
        private Long quantity;
        private Product product;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder quantity(Long quantity) {
            this.quantity = quantity;
            return this;
        }
        public Builder product(Product product) {
            this.product = product;
            return this;
        }
        public Option build() {
            return new Option(this);
        }
    }
}
