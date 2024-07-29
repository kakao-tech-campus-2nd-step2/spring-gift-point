package gift.domain;

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
public class UserInfo {

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


}
