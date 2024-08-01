package gift.domain;

import gift.exception.errorMessage.Messages;
import gift.exception.customException.CannotDeleteLastOptionException;
import gift.exception.customException.InsufficientQuantityException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "options")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "name", nullable = false)
    String name;
    @Column(name = "quantity")
    int quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    protected Option() {
    }

    public Option(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Option(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.quantity = builder.quantity;
        this.product = builder.product;
    }

    public Option(Long id, String name, int quantity, Product product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Option(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
        if (this.product != null) {
            this.product.addOption(this);
        }
    }

    public void validateOptionCountBeforeRemove(){
        int productOptionCount = this.product.getOptions().size();
        if(productOptionCount < 2){
            throw new CannotDeleteLastOptionException(Messages.CANNOT_DELETE_LAST_OPTION);
        }
    }

    public void remove() {
        validateOptionCountBeforeRemove();
        if (this.product != null) {
            this.product.removeOption(this);
        }
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

    public void setProduct(Product product) {
        this.product = product;
    }

    public void updateOption(String name, int quantity, Product product){
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public void subtract(int subtractQuantity){
        if(subtractQuantity > this.quantity){
            throw new InsufficientQuantityException(Messages.INSUFFICIENT_QUANTITY);
        }
        this.quantity = this.quantity - subtractQuantity;
    }

    public static class Builder {
        private Long id;
        private String name;
        private int quantity;
        private Product product;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Option build() {
            Option option = new Option(this);
            if (option.product != null && option.id == null) {
                this.product.addOption(option);
            }
            return option;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return Objects.equals(id, option.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
