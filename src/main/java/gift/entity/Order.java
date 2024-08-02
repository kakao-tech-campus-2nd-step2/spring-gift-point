package gift.entity;

import gift.dto.order.OrderRequestDTO;
import gift.dto.order.OrderResponseDTO;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name = "order_tb")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    Option option;

    int quantity;
    String message;
    int point;
    boolean receipt;
    String phone;

    int price;

    @CreatedDate
    LocalDateTime orderDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    public Order() {

    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Option getOption() {
        return option;
    }

    public String getMessage() {
        return message;
    }

    public int getPrice() {
        return price;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public Order(OrderRequestDTO orderRequestDTO, Product product, Option option, User user, int price) {
        this.option = option;
        this.product = product;
        this.quantity = orderRequestDTO.quantity();
        this.message = orderRequestDTO.message();
        this.user = user;
        this.point = orderRequestDTO.point();
        this.receipt = orderRequestDTO.receipt();
        if (receipt) this.phone = orderRequestDTO.phone();
        else this.phone = null;
        this.price = price;
    }

    public OrderResponseDTO toResponseDTO() {
        return new OrderResponseDTO(id, product.getId(), product.getName(), product.getImageUrl(), option.getId(), quantity, price, orderDateTime, message, true);
    }
}
