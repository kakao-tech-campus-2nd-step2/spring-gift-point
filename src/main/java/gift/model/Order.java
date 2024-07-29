package gift.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private SiteUser user;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    public Order() {
    }

    public Order(SiteUser user, Option option, int quantity, LocalDateTime orderDateTime) {
        this.user = user;
        this.option = option;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
    }

    public Long getId() {
        return id;
    }

    public SiteUser getUser() {
        return user;
    }

    public Option getOption() {
        return option;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(SiteUser user) {
        this.user = user;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }
}
