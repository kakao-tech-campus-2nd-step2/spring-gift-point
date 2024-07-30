package gift.model.order;

import gift.model.option.Option;
import gift.model.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @NotNull
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    @NotNull
    private Option option;

    @NotNull
    private int quantity;

    @CreatedDate
    private LocalDateTime orderDateTime;

    @NotNull
    private String message;

    protected Order() {
    }

    public Order(Long id, Product product, Option option, int quantity, LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.product = product;
        this.option = option;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public Order(Product product, Option option, int quantity, String message) {
        this.product = product;
        this.option = option;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Option getOption() {
        return option;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public Product getProduct() {
        return product;
    }
}
