package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Order_tb")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "optionId", nullable = false)
    private Option option;
    @Column
    LocalDateTime orderDateTime;
    @Column
    String message;

    public Order() {
    }

    public Order(Option option, LocalDateTime orderDateTime, String message) {
        this.option = option;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public Option getOption() {
        return option;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
