package gift.domain;

import gift.utils.TimeStamp;
import gift.utils.error.PointNotEnoughException;
import gift.utils.error.PointOverException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class UserInfo extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishes = new ArrayList<>();
    @OneToMany(mappedBy = "userInfo",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @Column(nullable = false)
    private int point;

    public UserInfo() {
    }

    public UserInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(id, userInfo.id) && Objects.equals(email,
            userInfo.email) && Objects.equals(password, userInfo.password)
            && Objects.equals(wishes, userInfo.wishes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }

    public void addWish(Wish wish) {
        wishes.add(wish);
        wish.setUserInfo(this);
    }

    public void removeWish(Wish wish) {
        wishes.remove(wish);
        wish.setUserInfo(null);
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setUserInfo(this);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setUserInfo(null);
    }
    public void minusPoint(int point,int totalPrice){
        if (this.point < point){
            throw new PointNotEnoughException("Point Not Enough");
        }
        if (totalPrice < point){
            throw new PointOverException("Point Over Exception");
        }
        this.point -= point;
    }

    public void plusPoint(int point){
        this.point+=point;
    }


}
