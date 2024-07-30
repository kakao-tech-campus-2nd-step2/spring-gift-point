package gift.order.domain;

import gift.member.domain.Member;
import gift.option.domain.Option;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private OrderCount count;
    @Embedded
    private OrderMessage message;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    private Option option;

    // JDBC 에서 엔티티 클래스를 인스턴스화할 때 반드시 기본 생성자와 파라미터 생성자가 필요하다
    public Order() {
    }

    public Order(Long id, OrderCount count, OrderMessage message, Member member, Option option) {
        this.id = id;
        this.count = count;
        this.message = message;
        this.member = member;
        this.option = option;
    }


    public Long getId() {
        return id;
    }

    public OrderMessage getMessage() {
        return message;
    }

    public OrderCount getCount() {
        return count;
    }

    public Member getMember() {
        return member;
    }

    public Option getOption() {
        return option;
    }

    public boolean checkNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Order item = (Order) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
