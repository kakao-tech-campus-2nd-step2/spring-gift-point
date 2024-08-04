package gift.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private Long optionId;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = true, length = 255)
    private String message;

    @Column(name = "order_date_time", nullable = false)
    private LocalDateTime orderDateTime;

    @Column(nullable = false)
    private boolean cashReceipt;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(nullable = false)
    private int paymentAmount;

    @Column(nullable = false)
    private int pointsUsed;

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", optionId=" + optionId +
            ", option=" + (option != null ? option.getId() : "null") +
            ", quantity=" + quantity +
            ", message='" + message + '\'' +
            ", orderDateTime=" + orderDateTime +
            ", cashReceipt=" + cashReceipt +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", paymentAmount=" + paymentAmount +
            ", pointsUsed=" + pointsUsed +
            '}';
    }
}