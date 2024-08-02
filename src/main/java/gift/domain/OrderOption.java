package gift.domain;

import gift.dto.orderOption.OrderOptionResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "order_option")
public class OrderOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "point", nullable = false)
    private int point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_email", nullable = false)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cash_receipt_id")
    private CashReceipt cashReceipt;

    @CreationTimestamp
    @Column(name = "orderDateTime", nullable = false)
    private Timestamp orderDateTime;

    protected OrderOption() {

    }

    public OrderOption(long productId, Option option, int quantity, String message, int point, Member member) {
        this.productId = productId;
        this.option = option;
        this.quantity = quantity;
        this.message = message;
        this.point = point;
        this.member = member;
    }

    public OrderOptionResponse toOrderOptionResponse() {
        return new OrderOptionResponse(id);
    }

    public void setCashReceipt(CashReceipt cashReceipt) {
        this.cashReceipt = cashReceipt;
    }
}
