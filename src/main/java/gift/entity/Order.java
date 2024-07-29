package gift.entity;

import gift.dto.option.OptionQuantityDTO;
import gift.dto.option.OrderResponseDTO;
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
    Option option;
    int quantity;
    String message;

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

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public Order(OptionQuantityDTO optionQuantityDTO, Option option, User user) {
        this.option = option;
        this.quantity = optionQuantityDTO.quantity();
        this.message = optionQuantityDTO.message();
        this.user = user;
    }

    public OrderResponseDTO toResponseDTO() {
        return new OrderResponseDTO(option.getId(), quantity, orderDateTime, message);
    }
}
