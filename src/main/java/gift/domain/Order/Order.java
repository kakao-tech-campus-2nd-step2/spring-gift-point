package gift.domain.Order;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long optionId;
    private String memberId;
    private Long quantity;
    private Date orderDateTime;
    private String message;

    public Order(Long id, Long optionId,String memberId, Long quantity, Date orderDateTime, String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.memberId = memberId;
    }

    public Order() {

    }
}
