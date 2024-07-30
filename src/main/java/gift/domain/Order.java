package gift.domain;

import gift.dto.OrderResponse;
import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long option_id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    private int quantity;

    private String message;

    private LocalDate ordered_at;

    public Order(Member member, Option option, Integer quantity, String message) {
        this.member = member;
        this.option = option;
        this.quantity = quantity;
        this.message = message;
    }

    @PrePersist
    public void prePersist() {
        this.ordered_at = LocalDate.now(); // 현재 날짜를 설정
    }

    public Long getId() {
        return option_id;
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
    public String getMessage() {
        return message;
    }
    public LocalDate getOrdered_at() {
        return ordered_at;
    }
}
