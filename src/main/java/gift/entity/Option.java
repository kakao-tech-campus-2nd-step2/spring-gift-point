package gift.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import gift.dto.request.OptionRequestDTO;
import gift.exception.optionException.OptionException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="option", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "product_id"})})
public class Option {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = false, nullable = false)
    @Size(max = 50, message = "Option name must be 50 characters or less.")
    @Pattern(regexp = "^[\\w\\s\\[\\]\\(\\)\\+\\-\\&\\/]*$", message = "Option name contains invalid characters.")
    private String name;

    @Column(name="quantity")
    @Min(value = 1, message = "Quantity must be at least 1.")
    @Max(value = 100000000, message = "Quantity must be less than 100,000,000.")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "option", orphanRemoval = true)
    private List<Wish> wishes = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "option", orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public Option() {

    }

    public Option(String name, int quantity, Product product) {
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

    public void updateOption(OptionRequestDTO optionRequestDTO) {
        this.name = optionRequestDTO.name();
        this.quantity = optionRequestDTO.quantity();
    }

    public void subtract(int quantity){
        if(this.quantity>=quantity){
            this.quantity-=quantity;
            return;
        }
        throw new OptionException("상품 수보다 많은 수량을 입력하였습니다.");
    }

}
