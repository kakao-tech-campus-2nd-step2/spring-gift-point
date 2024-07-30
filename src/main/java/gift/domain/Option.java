package gift.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "option", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_id", "name"})
})
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private Product product;

    protected Option() {
    }

    private Option(OptionBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.quantity = builder.quantity;
        this.product = builder.product;
    }

    public void subtractQuantity(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("수량이 부족합니다.");
        }
        this.quantity -= quantity;
    }

    public boolean quantityLessThan(int quantity) {
        return this.quantity < quantity;
    }

    public OptionBuilder toBuilder() {
        return new OptionBuilder()
            .id(this.id)
            .name(this.name)
            .quantity(this.quantity)
            .product(this.product);
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

    public Product getProduct() {
        return product;
    }

    public static class OptionBuilder {
        private Long id;
        private String name;
        private int quantity;
        private Product product;

        public OptionBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OptionBuilder name(String name) {
            this.name = name;
            return this;
        }

        public OptionBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public OptionBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public Option build() {
            return new Option(this);
        }
    }
}
