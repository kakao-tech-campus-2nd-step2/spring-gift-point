package gift.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import gift.dto.OptionRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "product", nullable = false)
    @JsonProperty("product")
    private Product product;

    public Option() {

    }

    public Option(OptionRequest optionRequest, Product product) {
        this.name = optionRequest.getName();
        this.quantity = optionRequest.getQuantity();
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
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
