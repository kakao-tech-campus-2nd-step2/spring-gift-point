package gift.doamin.order.entity;

import gift.doamin.product.entity.Option;
import gift.doamin.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    @ManyToOne(optional = false)
    private Option option;

    @Column(nullable = false)
    private Integer quantity;

    String message;

    LocalDateTime orderDateTime;

    protected Order() {
    }

    public Order(User sender, User receiver, Option option, Integer quantity, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.option = option;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public Option getOption() {
        return option;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }
}
