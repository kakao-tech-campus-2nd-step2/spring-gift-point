package gift.domain;

import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime orderDateTime;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private int orderPrice;

    protected Order(){

    }

    public Order(User user, Option option, int quantity, String message, int orderPrice) {
        this.user = user;
        this.option = option;
        this.quantity = quantity;
        this.message = message;
        this.orderPrice = orderPrice;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public User getUser() {
        return user;
    }

    public Option getOption() {
        return option;
    }

    public String getMessage() {
        return message;
    }
    public int getOrderPrice() {
        return orderPrice;
    }
}
