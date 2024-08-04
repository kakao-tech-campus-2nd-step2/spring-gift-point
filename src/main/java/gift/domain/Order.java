package gift.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "ORDERS")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPTION_ID", nullable = false)
    private Option option;

    @Column(nullable = false)
    private int quantity;
    private String message;
    private int point;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime orderDateTime;

    public Order() {
    }

    private Order(Builder builder) {
        this.member = builder.member;
        this.option = builder.option;
        this.quantity = builder.quantity;
        this.message = builder.message;
        this.point = builder.point;
    }

    // 빌더 클래스
    public static class Builder {
        private Member member;
        private Option option;
        private int quantity;
        private String message;
        private int point;

        public Builder member(Member member) {
            this.member = member;
            return this;
        }

        public Builder option(Option option) {
            this.option = option;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder point(int point) {
            this.point = point;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
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

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public int getPoint() {
        return point;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }
}
