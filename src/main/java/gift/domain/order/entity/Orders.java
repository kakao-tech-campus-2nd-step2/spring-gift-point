package gift.domain.order.entity;

import gift.domain.member.entity.Member;
import gift.domain.option.entity.Option;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Column
    private int quantity;
    @Column
    private String orderDateTime;
    @Column
    private String message;

    protected Orders() {
    }

    public Orders(Option option, Member member, int quantity, String orderDateTime,
        String message) {
        this(null, option, member, quantity, orderDateTime, message);
    }

    public Orders(Long id, Option option, Member member, int quantity, String orderDateTime,
        String message) {
        this.id = id;
        this.option = option;
        this.member = member;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
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

    public Member getMember() {
        return member;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
