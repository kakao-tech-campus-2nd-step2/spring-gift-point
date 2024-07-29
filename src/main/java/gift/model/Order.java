package gift.model;

import gift.exception.InputException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false,
    foreignKey = @ForeignKey(name = "fk_order_option_id_ref_option_id"))
    private Options options;
    @Column(nullable = false)
    private Integer quantity;
    private String message;

    protected Order() {
    }

    public Order(Long id, Long memberId, Options options, Integer quantity, String message) {
        super();
        validateQuantity(quantity);
        validateMessage(message);
        this.id = id;
        this.memberId = memberId;
        this.options = options;
        this.quantity = quantity;
        this.message = message;
    }

    public Order(Long memberId, Options options, Integer quantity, String message) {
        super();
        validateQuantity(quantity);
        validateMessage(message);
        this.memberId = memberId;
        this.options = options;
        this.quantity = quantity;
        this.message = message;
    }

    public Order(Long id, Long memberId, Options options, Integer quantity, String message,
        LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        validateQuantity(quantity);
        validateMessage(message);
        this.id = id;
        this.memberId = memberId;
        this.options = options;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Options getOptions() {
        return options;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity < 0 || quantity > 100000000) {
            throw new InputException("수량은 1개~1억개 사이여야 합니다.");
        }
    }

    private void validateMessage(String message) {
        if (message != null && message.length() > 100) {
            throw new InputException("주문 메시지는 100자 이하로 입력할 수 있습니다.");
        }
    }

}
