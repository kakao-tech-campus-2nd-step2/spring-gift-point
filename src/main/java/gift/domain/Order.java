package gift.domain;

import gift.dto.MemberDto;
import gift.dto.OrderDto;
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

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private int point;

    public Order() {
    }

    public Order(Member member, Option option, int quantity, String message) {
        this.member = member;
        this.option = option;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = LocalDateTime.now();
    }

    public Order(Option option, Member member, int quantity,
        String message, int point) {
        this.option = option;
        this.member = member;
        this.quantity = quantity;
        this.orderDateTime = LocalDateTime.now();
        this.message = message;
        this.point = point;
    }

    public MemberDto memberDto(){
        return new MemberDto(this.member.getId(), this.member.getEmail());
    }

    public OrderDto toOrderDto() {
        return new OrderDto(this.id, this.memberDto(), this.option.getId(), this.quantity, this.orderDateTime,
            this.message);
    }
}
