package gift.domain;

import gift.dto.request.OrderRequest;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long optionId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer point;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private boolean receipt;

    @Column(nullable = false)
    private boolean success;

    public Order() { }

    public Order(Long optionId, Integer quantity, LocalDateTime orderDateTime, String message, Long productId, Integer point, String phone, Integer price, boolean receipt, boolean success) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.productId = productId;
        this.point = point;
        this.phone = phone;
        this.price = price;
        this.receipt = receipt;
        this.success = success;
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getPoint() {
        return point;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getPrice() {
        return price;
    }

    public boolean isReceipt() {
        return receipt;
    }

    public boolean isSuccess() {
        return success;
    }

    public static Order from(OrderRequest orderRequest, int price, boolean isSuccess) {
        return new Order(orderRequest.getOptionId(),
                orderRequest.getQuantity(),
                LocalDateTime.now(),
                orderRequest.getMessage(),
                orderRequest.getProductId(),
                orderRequest.getPoint(),
                orderRequest.getPhone(),
                price,
                orderRequest.isReceipt(),
                isSuccess);
    }

}