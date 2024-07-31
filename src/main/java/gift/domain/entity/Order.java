package gift.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "`Order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Option option;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime orderDateTime;

    @Column(nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    protected Order() {
    }

    public Order(Member member, Option option, Integer quantity, String message) {
        this.member = member;
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

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public Member getMember() {
        return member;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", option=" + option +
            ", quantity=" + quantity +
            ", orderDateTime=" + orderDateTime +
            ", message='" + message + '\'' +
            ", member=" + member +
            '}';
    }
}
