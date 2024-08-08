package gift.domain;

import gift.dto.request.OrderRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static gift.constant.ErrorMessage.POSITIVE_NUMBER_REQUIRED_MSG;

@Entity
@Table(name = "order_details")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Positive(message = POSITIVE_NUMBER_REQUIRED_MSG)
    @Column(nullable = false)
    private int quantity;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDateTime;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Integer savedPoint;

    public Order() {

    }

    public Order(Option option, Member member, OrderRequest orderRequest, Integer savedPoint) {
        this.option = option;
        this.member = member;
        this.quantity = orderRequest.quantity();
        this.message = orderRequest.message();
        this.savedPoint = savedPoint;
    }

    public Long getId() {
        return id;
    }

    public Option getOption() {
        return option;
    }

    public Member getMember() {
        return member;
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

    public Integer getSavedPoint() {
        return savedPoint;
    }
}
