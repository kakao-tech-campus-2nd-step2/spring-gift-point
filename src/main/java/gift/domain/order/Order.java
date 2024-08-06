package gift.domain.order;

import gift.domain.member.Member;
import gift.domain.option.Option;
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
    private Long id;
    @JoinColumn(nullable = false)
    @ManyToOne
    private Member member;
    @JoinColumn(nullable = false)
    @ManyToOne
    private Option option;
    @Column(nullable = false)
    private Long quantity;
    @Column
    private String message;
    @Column
    private final LocalDateTime orderDateTime = LocalDateTime.now();

    public Order() {

    }

    public Order(Member member, OrderRequest orderRequest) {
        this.member = new Member(member.getId());
        this.option = new Option(orderRequest.optionId());
        this.quantity = orderRequest.quantity();
        this.message = orderRequest.message();
    }

    public OrderResponse toOrderResponse() {
        return new OrderResponse(id, option.getId(), quantity, orderDateTime, message);
    }
}
