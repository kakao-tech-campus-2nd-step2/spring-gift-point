package gift.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_\\s]*$")
    @Column(nullable = false, length = 50)
    private String name;

    @NotNull
    @Min(1)
    @Max(99999999)
    @Column(nullable = false)
    private Integer quantity;

    @Getter
    @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Option() {}

    public Option(String name, Integer quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public @NotBlank @Size(max = 50) @Pattern(regexp = "^[a-zA-Z0-9가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_\\s]*$") String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setName(@NotBlank @Size(max = 50) @Pattern(regexp = "^[a-zA-Z0-9가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_\\s]*$") String name) {
        this.name = name;
    }

    public @NotNull @Min(1) @Max(99999999) Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull @Min(1) @Max(99999999) Integer quantity) {
        this.quantity = quantity;
    }

    public void subtractQuantity(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("Insufficient quantity");
        }
        this.quantity -= quantity;
    }
}