package gift.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Column(nullable = false)
    private String message;

    public Order() {
    }

    public Order(Option option, int quantity, String message) {
        this.option = option;
        this.quantity = quantity;
        this.orderDateTime = LocalDateTime.now();
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Option getOption() {
        return option;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}
