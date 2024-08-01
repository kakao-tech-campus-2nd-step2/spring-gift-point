package gift.domain;

import gift.classes.Exceptions.OptionException;
import gift.dto.OptionDto;
import gift.dto.ProductDto;
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

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Option() {
    }

    public Option(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Option(Long id, String name, int quantity, Product product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
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

    public ProductDto getProductDto() {
        return new ProductDto(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl());
    }

    public OptionDto getOptionDto() {
        return new OptionDto(this.getId(), this.getName(), this.getQuantity());
    }

    public void update(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void decrementAmount(int quantity) {
        if (quantity <= 0) {
            throw new OptionException("The amount to be deducted must be greater than zero.");
        }
        if (this.quantity >= quantity) {
            this.quantity -= quantity;
        } else {
            throw new OptionException("Deduction failed due to insufficient quantity.");
        }
    }
}
