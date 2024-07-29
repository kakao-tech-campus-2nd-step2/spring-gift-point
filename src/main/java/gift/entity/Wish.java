package gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Wish entity representing a user's wish for a product")
@Entity
@Table(name = "wishes")
public class Wish {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Schema(description = "Unique identifier of the wish", example = "1")
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    @Schema(description = "User who created the wish")
    private User user;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    @Schema(description = "Product that is wished for")
    private Product product;
    @NotNull
    @Schema(description = "Number of products wished for", example = "3")
    private Integer number;

    protected Wish() {
    }

    public Wish(User user, Product product, int number) {
        this.user = user;
        this.product = product;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public int getNumber() {
        return number;
    }

    public void subtractNumber(Integer number) {
        this.number -= number;
    }

    public boolean sameProduct(Product product) {
        return this.product == product;
    }

    public boolean checkLeftWishNumber() {
        return this.number <= 0;
    }
}
