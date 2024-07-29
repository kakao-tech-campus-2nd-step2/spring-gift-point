package gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "주문 id", nullable = false, example = "1")
    private Long id;
    @Schema(description = "상품 id", nullable = false, example = "1")
    private Long product_id;
    @Schema(description = "옵션 id", nullable = false, example = "1")
    private Long option_id;
    @Schema(description = "주문 수량", nullable = false, example = "5")
    private int quantity;
    @Schema(description = "주문 메시지", nullable = false, example = "주문 메시지 입니다")
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Order(OrderDTO orderDTO) {
        this.product_id = orderDTO.getProductId();
        this.option_id = orderDTO.getOptionId();
        this.quantity = orderDTO.getQuantity();
        this.message = orderDTO.getMessage();
    }

    public Order() {

    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
