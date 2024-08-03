package gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Schema(description = "Wish entity representing a user's wish for a product")
@Entity
@Table(name = "wishes")
@EntityListeners(AuditingEntityListener.class)
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

    @Schema(description = "Date and time when the wish was created", example = "2024-07-28T17:04:18.834374")
    @CreatedDate
    private LocalDateTime createdDate;

    protected Wish() {

    }

    public Wish(User user, Product product) {
        this.user = user;
        this.product = product;
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

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public boolean sameProduct(Product product) {
        return this.product == product;
    }

}
