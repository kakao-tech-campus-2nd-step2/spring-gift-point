package gift.domain;

import gift.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "orders")
@SQLDelete(sql = "UPDATE orders SET deletion_date = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deletion_date IS NULL")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Min(1)
    @Column(nullable = false)
    private int quantity;

    private String message;

    @Column(nullable = false, name = "used_point")
    @ColumnDefault("0")
    private int usedPoint;

    @Column(nullable = false, name = "received_point")
    private int receivedPoint;

    @Column(nullable = false, name = "total_price")
    private int totalPrice;

    public Order() {
    }

    public Order(Option option, AppUser user, int quantity, String message, int usedPoint, int price) {
        this.option = option;
        this.user = user;
        this.quantity = quantity;
        this.message = message;
        this.usedPoint = usedPoint;
        this.receivedPoint = Math.round(price * 0.05f);
        this.totalPrice = price - usedPoint;
    }

    public Long getId() {
        return id;
    }

    public Option getOption() {
        return option;
    }

    public AppUser getUser() {
        return user;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getReceivedPoint() {
        return receivedPoint;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
