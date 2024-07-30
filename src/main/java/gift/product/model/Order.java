package gift.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Option option;

    @Positive
    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Column(length = 200)
    private String message;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member orderer;

    public Order() {

    }

    public Order(Option option, int quantity, String message, Member orderer) {
        this.option = option;
        this.quantity = quantity;
        this.orderDateTime = LocalDateTime.now();
        this.message = message;
        this.orderer = orderer;
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

    public String getOrderDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return orderDateTime.format(formatter);
    }

    public String getMessage() {
        return message;
    }

    public Member getOrderer() {
        return orderer;
    }
}
