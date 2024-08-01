package gift.model;

import gift.exceptions.CustomException;
import jakarta.persistence.*;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

@Entity
@Table(name = "options")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    @Min(1)
    @Max(99999999)
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishes;

    protected Option() { }

    public Option(String name, Long quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public void subtract(Long amount) {
        if  (amount >= this.quantity) {
            throw CustomException.quantityInvalidException();
        }
        this.quantity -= amount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}
