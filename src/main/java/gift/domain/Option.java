package gift.domain;

import gift.classes.Exceptions.OptionException;
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
    private int amount;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Option() {
    }

    public Option(String name, int amount, Product product) {
        this.name = name;
        this.amount = amount;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public ProductDto getProductDto() {
        return new ProductDto(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl(), product.getCategoryDto(), product.getOptionDtos());
    }

    public void update(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public void decrementAmount(int quantity) {
        if (quantity <= 0) {
            throw new OptionException("The amount to be deducted must be greater than zero.");
        }
        if (amount >= quantity) {
            amount -= quantity;
        } else {
            throw new OptionException("Deduction failed due to insufficient quantity.");
        }
    }
}
