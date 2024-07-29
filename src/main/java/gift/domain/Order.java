package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    Option option;
    @ManyToOne(fetch = FetchType.LAZY)
    UserInfo userInfo;
    @Column(nullable = false)
    int quantity;
    @Column(nullable = false)
    LocalDateTime orderDateTime;
    @Column
    String message;

    public Order(int quantity, LocalDateTime orderDateTime, String message) {
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public Order() {

    }

    public Long getId() {
        return id;
    }

    public Option getOption() {
        return option;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setOption(Option option) {
        this.option = option;
    }

}
