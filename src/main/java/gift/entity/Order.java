package gift.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    String message;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "uesr_id")
    private User user;
    @NotNull
    private Integer quantity;
    @NotNull
    private LocalDateTime orderDateTime;

    public Order(Option option, User user, Integer quantity, LocalDateTime orderDateTime,
        String message) {
        this.option = option;
        this.user = user;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    protected Order() {

    }

    public Long getId() {
        return id;
    }

    public Option getOption() {
        return option;
    }

    public User getUser() {
        return user;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
