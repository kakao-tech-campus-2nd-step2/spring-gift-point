package gift.domain.order;

import gift.domain.product.option.ProductOption;
import gift.domain.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_option_id", nullable = false)
    private ProductOption productOption;

    private Long quantity;
    private String message;

    @Column(name = "order_date_time")
    private LocalDateTime orderDateTime;

    // Default constructor
    protected Order() {}

    // Constructor with all fields
    public Order(User user, ProductOption productOption, Long quantity, String message, LocalDateTime orderDateTime) {
        this.user = user;
        this.productOption = productOption;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
    }


    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public ProductOption getProductOption() {
        return productOption;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }
}
