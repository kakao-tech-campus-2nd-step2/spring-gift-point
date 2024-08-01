package gift.doamin.wishlist.entity;

import gift.doamin.product.entity.Option;
import gift.doamin.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Option option;

    @Column(nullable = false)
    private Integer quantity;


    public Wish(User user, Option option, Integer quantity) {
        this.user = user;
        this.option = option;
        this.quantity = quantity;
    }

    protected Wish() {

    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Option getOption() {
        return option;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void subtract(Integer quantity) {
        if (this.quantity <= quantity) {
            throw new IllegalArgumentException("위시 수량보다 많은 값을 차감할 수 없습니다.");
        }
        this.quantity -= quantity;
    }
}
