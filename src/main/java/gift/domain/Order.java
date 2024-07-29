package gift.domain;

import gift.utils.TimeStamp;
import jakarta.persistence.*;

@Entity
@Table(name = "ORDERS")
public class Order extends TimeStamp {

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

    public Order() {
    }

    private Order(Builder builder) {
        this.member = builder.member;
        this.option = builder.option;
        this.quantity = builder.quantity;
        this.message = builder.message;
    }

    // 빌더 클래스
    public static class Builder {
        private Member member;
        private Option option;
        private int quantity;
        private String message;

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
}
