package gift.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import gift.dto.order.OrderDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "주문 id", nullable = false, example = "1")
    private Long id;
    @Schema(description = "상품 id", nullable = false, example = "1")
    @JsonProperty("product_id")
    private Long productId;
    @Schema(description = "옵션 id", nullable = false, example = "1")
    @JsonProperty("option_id")
    private Long optionId;
    @Schema(description = "주문 수량", nullable = false, example = "5")
    private int quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate orderDateTime;
    @Schema(description = "주문 메시지", nullable = false, example = "주문 메시지 입니다")
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Order(OrderDTO orderDTO) {
        this.productId = orderDTO.getProduct_id();
        this.optionId = orderDTO.getOption_id();
        this.quantity = orderDTO.getQuantity();
        this.orderDateTime = LocalDate.now();
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long product_id) {
        this.productId = product_id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long option_id) {
        this.optionId = option_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDate orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
