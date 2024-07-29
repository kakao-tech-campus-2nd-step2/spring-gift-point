package gift.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id", nullable = false)
    private ProductOption productOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private int quantity;
    private String message;
    private LocalDateTime orderDateTime;

    public Order() {
    }

    public Order(ProductOption productOption, Member member, int quantity, String message, LocalDateTime orderDateTime) {
        this.productOption = productOption;
        this.member = member;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
    }

    public Long getId() {
        return id;
    }

    public ProductOption getProductOption() {
        return productOption;
    }

    public Member getMember() {
        return member;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }
}
