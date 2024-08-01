package gift.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "product_id"})})
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[\\w\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]+$", message = "Invalid characters in option name")
    private String name;

    @Min(1)
    @Max(999999999)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Option() {
    }

    public Option(String name, int quantity, Product product){
        if(name.length()>50){
            throw new IllegalArgumentException("Option name must be less than 50 characters");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (quantity > 100_000_000) {
            throw new IllegalArgumentException("Quantity must be less than or equal to 100,000,000");
        }
        if(product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if(!name.matches("^[\\w\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]+$")) {
            throw new IllegalArgumentException("Invalid characters in option name");
        }
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void subtractQuantity(int quantity) {
        if (this.quantity < quantity) {
            throw new RuntimeException("Not enough quantity available");
        }
        this.quantity -= quantity;
    }
}
