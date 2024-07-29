package gift.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = AuditingEntityListener.class)
@Table(name = "orders")
@Entity
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "option_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Option option;

    @Column(nullable = false)
    private Long quantity;

    private String message;

    @CreatedDate
    private LocalDateTime orderDateTime;

    public Order() {
    }

    public Order(Option option) {
        this.option = option;
    }

    public Order(Option option, Long quantity, String message) {
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
