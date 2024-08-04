package gift.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @Column(nullable = false, name = "order_quantity")
    private Integer orderQuantity;

    @Column(nullable = false, name = "order_message")
    private String message;

    @Column(nullable = false, updatable = false, name = "order_date")
    private LocalDateTime orderDateTime;

    @Column(nullable = false, name = "total_price")
    private Long totalPrice;

    @Column(nullable = false, name="order_success")
    private boolean orderSuccess;


    public Order() {
    }

    public Order(Member member, Option option, Integer orderQuantity, String message, LocalDateTime orderDateTime, Long totalPrice, boolean orderSuccess) {
        this.member = member;
        this.option = option;
        this.orderQuantity = orderQuantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
        this.totalPrice = totalPrice;
        this.orderSuccess = orderSuccess;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Option getOption() {
        return option;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public Long getTotalPrice() { return totalPrice; }

    public boolean getOrderSuccess() { return orderSuccess; }
}
