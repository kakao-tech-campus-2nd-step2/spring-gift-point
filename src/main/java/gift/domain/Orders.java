package gift.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Orders extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Column(nullable = true)
    private String message;

    public Orders() {}

    public Orders(Member member, Option option, int quantity, LocalDateTime orderDateTime, String message) {
        this.member = member;
        this.option = option;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public Member getMember() {
        return member;
    }

    public Option getOption() {
        return option;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public Orders withMember(Member member) {
        return new Orders(member, this.option, this.quantity, this.orderDateTime, this.message);
    }

    public Orders withOption(Option option) {
        return new Orders(this.member, option, this.quantity, this.orderDateTime, this.message);
    }

    public Orders withQuantity(int quantity) {
        return new Orders(this.member, this.option, quantity, this.orderDateTime, this.message);
    }

    public Orders withOrderDateTime(LocalDateTime orderDateTime) {
        return new Orders(this.member, this.option, this.quantity, orderDateTime, this.message);
    }

    public Orders withMessage(String message) {
        return new Orders(this.member, this.option, this.quantity, this.orderDateTime, message);
    }
}
