package gift.api.order.domain;

import gift.api.member.domain.Member;
import gift.api.option.domain.Option;
import gift.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_order_member_id_ref_member_id"))
    private Member member;
    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false, foreignKey = @ForeignKey(name = "fk_order_option_id_ref_option_id"))
    private Option option;
    private Integer quantity;
    @CreatedDate
    @Column(nullable = false)
    private Timestamp orderDateTime;
    private String message;

    protected Order() {
    }

    public Order(Member member, Option option, Integer quantity, String message) {
        this.member = member;
        this.option = option;
        this.quantity = quantity;
        this.message = message;
    }

    public Option getOption() {
        return option;
    }

    public Long getOptionId() {
        return option.getId();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getOptionQuantity() {
        return option.getQuantity();
    }

    public Timestamp getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
