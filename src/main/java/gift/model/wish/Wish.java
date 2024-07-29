package gift.model.wish;

import gift.model.gift.Gift;
import gift.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "wish")
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "gift_id")
    @NotNull
    private Gift gift;

    @NotNull
    private int quantity;

    public Wish() {
    }

    public Wish(User user, Gift gift, int quantity) {
        this.user = user;
        this.gift = gift;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Gift getGift() {
        return gift;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity() {
        this.quantity++;
    }

    public void modifyQuantity(int quantity) {
        this.quantity = quantity;
    }
}
