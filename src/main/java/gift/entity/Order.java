package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderProductOption> orderProductOptions = new ArrayList<>();

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "message")
    private String message;

    protected Order() {
    }

    public Order(Member member, int quantity, String message) {
        this.member = member;
        this.quantity = quantity;
        this.message = message;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderProductOption> getOrderProducts() {
        return orderProductOptions;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}
