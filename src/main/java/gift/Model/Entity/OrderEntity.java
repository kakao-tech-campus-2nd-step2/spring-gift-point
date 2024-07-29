package gift.Model.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;

@Entity
@Table(name="orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="option_id")
    private OptionEntity optionEntity;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "order_date_time")
    private LocalDateTime orderDateTime;

    @Column(name = "message")
    private String message;

    public OrderEntity(){}

    public OrderEntity(OptionEntity optionEntity, Long quantity, LocalDateTime orderDateTime, String message){
        this.optionEntity = optionEntity;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public OptionEntity getOptionEntity() {
        return optionEntity;
    }

    public void setOptionEntity(OptionEntity optionEntity) {
        this.optionEntity = optionEntity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
