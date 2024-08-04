package gift.model.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import gift.model.user.User;
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
    private Long product_id;
    @Schema(description = "옵션 id", nullable = false, example = "1")
    private Long option_id;
    @Schema(description = "주문 수량", nullable = false, example = "5")
    private int quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate orderDateTime;
    @Schema(description = "주문 메시지", nullable = false, example = "주문 메시지 입니다")
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Order(OrderDTO orderDTO) {
        this.product_id = orderDTO.getProduct_id();
        this.option_id = orderDTO.getOption_id();
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

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Long getOption_id() {
        return option_id;
    }

    public void setOption_id(Long option_id) {
        this.option_id = option_id;
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
